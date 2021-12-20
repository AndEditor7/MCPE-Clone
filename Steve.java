package com.andedit.arcubit;

import static com.andedit.arcubit.world.World.world;
import static com.andedit.arcubit.options.Options.viewBob;
import static com.andedit.arcubit.options.Options.yInvert;
import static com.andedit.arcubit.options.Options.sens;
import static com.andedit.arcubit.options.Options.split;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.entity.EntityMob;
import com.andedit.arcubit.entity.ItemEnitiy;
import com.andedit.arcubit.entity.Player;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.graphics.Breaking;
import com.andedit.arcubit.graphics.Hand;
import com.andedit.arcubit.handles.Audio;
import com.andedit.arcubit.handles.Controllor;
import com.andedit.arcubit.handles.Inputs;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.RayContext.HitType;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolItem;
import com.andedit.arcubit.ui.InGame;
import com.andedit.arcubit.ui.Inventory;
import com.andedit.arcubit.ui.actors.Hotbar;
import com.andedit.arcubit.utils.Camera;
import com.andedit.arcubit.utils.Util;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/** In steve's mind. */
public class Steve implements Serial {
	public final TheGame game;
	
	public final Camera camera = new Camera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	public final Controllor controllor;
	public final Hotbar hotbar;
	public final Hand hand;
	public final Breaking breaking = new Breaking();
	public final Inventory inventory;
	public final Player player;
	
	public Steve(final TheGame game, boolean isLoad) {
		this.game = game;
		
		controllor = new Controllor();
		inventory = new Inventory(hotbar = new Hotbar(this, hand = new Hand(), isLoad), isLoad);
		player = new Player(this, isLoad);
		
		camera.near = 0.1f;
		camera.far = 500f;
	}
	
	private boolean isLeft;	
	private float swing;

	public void tick(final float delta) {
		if (game.isPlaying() && !player.isDying())  {
			if (!Util.isDesktop() || Util.isCatched()) {
				float scale = Util.isDesktop() ? 0.2f : 15.0f * delta;// * 0.7f;
				camera.yaw += controllor.deltaX * scale * sens.value;
				camera.pitch += controllor.deltaY * scale * sens.value * (yInvert.value ? -1f : 1f);
				controllor.deltaX = 0;
				controllor.deltaY = 0;
			}
		}
	}

	public void update() {
		if (Util.isDesktop() && Gdx.input.isKeyJustPressed(Keys.ALT_LEFT)) {
			useMouse(Util.isCatched());
		}
		
		controllor.update();
		if (controllor.isMining && controllor.isPressDown) {
			hand.swing();
		}

		final Vector3 vel = player.vel;
		final float speed = Util.sqrt(vel.x * vel.x + vel.z * vel.z) * 4f;
		player.update();
		process();
		
		if (player.isWalking() && controllor.isMoving() && !player.isDying()) {
			if (isLeft) {
				swing -= speed;
				if (swing < -MathUtils.PI) {
					isLeft = false;
					swing += speed;
				}
			} else {
				swing += speed;
				if (swing > MathUtils.PI) {
					isLeft = true;
					swing -= speed;
				}
				
			}
		} else {
			swing = MathUtils.lerp(swing, 0, 0.1f);
		}
		if (!viewBob.value) swing = 0;
		
		final float sin = MathUtils.sinDeg(camera.yaw+90f);
		final float cos = MathUtils.cosDeg(camera.yaw+90f);
		camera.position.add(sin*swing*0.01f, 0, cos*swing*0.01f);
		camera.updateRotation(Math.abs(MathUtils.cos(swing/2f)*0.4f));
		camera.update();		
	}
	
	private static final Vector3 tmp = new Vector3();
	
	public void respawn() {
		player.health = player.getMaxHealth();
		game.manager.setUI(InGame.class);
		final int s = World.SIZE/2;
		player.setPos(s, World.HEIGHT*0.8f, s);
		for (int y = World.HEIGHT; y != 0; y--) {
			if (world.getBlock(s, y, s) != Blocks.AIR) {
				player.setPos(s+0.5f, y+1.1f, s+0.5f);
				break;
			}
		}
	}
	
	private static final Ray rayTemp = new Ray();
	public void process() {
		final Ray ray;
		if (Util.isCatched() || split.value) {
			ray = rayTemp.set(camera.position, camera.direction);
		} else {
			ray = camera.getPickRay(controllor.lastX, controllor.lastY);
		}
		
		if (breaking.cooldown > 0) {
			breaking.cooldown--;
		}
		
		if (game.isPlaying() && !player.isDying()) {
			final RayContext context = world.shoot(ray);
			final HitType hitType = context.getHitType();
			final Item item = hotbar.getItem();
			final boolean onHold = controllor.isOnHold();
			final boolean allowPlace = controllor.allowPlace();
			
			// Hit entity.
			if (Util.isDesktop() ? Inputs.isButtonJustPressed(Buttons.LEFT) : allowPlace) {
				hand.swing();
				if (hitType == HitType.ENTITY && context.entityHit instanceof EntityMob) {
					if (((EntityMob)context.entityHit).hit(player, item == null ? 1 : item.getHitPoint())) {
						if (item instanceof ToolItem) {
							((ToolItem)item).takeDamage();
						}
					}
				}
			}
			
			if (onHold && item != null) {
				item.onUse(this, context, false);
			}
			
			// No block found.
			if (hitType != HitType.BLOCK) {
				breaking.process = 0;
				breaking.cooldown = 0;
				if (allowPlace && item != null) item.onUse(this, context, true);
			} else {
				if (allowPlace) {
					if (!context.blockHit.onClick(context.in, this)) {
						if (item != null) {
							item.onUse(this, context, true);
							hand.swing();
						}
					}
				}
			}			
			
			if (game.isSurvival) { // Survival control.
				if (controllor.isPressDown && breaking.cooldown == 0 && hitType == HitType.BLOCK) {
					if (Inputs.isButtonPressed(Buttons.LEFT) || controllor.isMining) {
						mining(context);
					} else {
						breaking.process = 0;
					}
				} else {
					breaking.process = 0;
				}
			} else { // Creative control.
				if (Util.isDesktop() || controllor.isPressDown) {
					if (controllor.allowBreak()) {
						hand.swing();
						Block.destroy(context, false);
					}
				}
			}
		}
	}
	
	private int soundTimer;
	private void mining(RayContext context) {
		final Block block = context.blockHit;
		if (block == null) return;
		
		final BlockType type = block.getBlockType(context.in);
		final Audio audio = type.audio;
		if (++soundTimer > 11) {
			soundTimer = 0;
			audio.play(0.25f, 0.5f);
		}
		
		hand.swing();
		final Item item = hotbar.getItem();
		if (breaking.pos.equals(context.in)) {
			float speedMultiplier = 1.0f;
			boolean canHarvest = false;
			int miningLevel = block.getMiningLevel(context.in);
			if (miningLevel == 0) {
				canHarvest = true;
			} else if (miningLevel == -1) {
				return;
			}
			
			ToolItem tool = null;
			if (item instanceof ToolItem) {
				tool = (ToolItem) item;
				if (tool.toolType == block.getToolType(context.in) && tool.material.level >= miningLevel) {
					speedMultiplier = tool.material.speed;
					canHarvest = true;
				}
			}
			
			float damage = speedMultiplier / block.getHardness(context.in);
			breaking.process += damage / (canHarvest ? 90f : 300f);
			
			if (breaking.process > 1.0f) {
				Block.destroy(context, canHarvest);
				breaking.process = 0;
				breaking.cooldown = 12;
				soundTimer = 3;
				type.getBreakSound().play(1, type == BlockType.FIRE?2:0.8f);
				if (tool != null) {
					tool.takeDamage();
				}
			}
		} else {
			breaking.pos.set(context.in);
			breaking.process = 0;
		}
	}
	
	public void useMouse(boolean bool) {
		if (!Util.isDesktop()) return;
		
		if (bool) {
			Gdx.input.setCursorCatched(false);
			Gdx.input.setCursorPosition(Util.getW()/2, Util.getH()/2);
		} else {
			Gdx.input.setCursorCatched(true);
			Gdx.input.setCursorPosition(0, 0);
		}
		
		controllor.deltaX = 0;
		controllor.deltaY = 0;
		controllor.lastX = 0;
		controllor.lastY = 0;
	}
	
	public boolean addItem(Item item) {
		return inventory.addItem(item);
	}
	
	public void dropItem(Item item) {
		ItemEnitiy enitiy = new ItemEnitiy(tmp.set(camera.position).sub(0, 0.5f, 0), item.clone());
		enitiy.vel.add(MathUtils.sinDeg(camera.yaw)*0.13f, 0.03f-(MathUtils.sinDeg(camera.pitch)*0.15f), MathUtils.cosDeg(camera.yaw)*0.13f);
		enitiy.cooldown = 120;
		world.addEntity(enitiy);
		item.remove();
		Sounds.POP.play();
	}
	
	public void render(float delta) {
		hand.render(camera.position, swing, delta);
	}
	
	public void resize(int width, int height) {
		hand.resize(width, height);
	}

	@Override
	public void save(Properties props) {
		inventory.save(props.newProps("inventory"));
		hotbar.save(props.newProps("hotbar"));
		player.save(props.newProps("player"));
		
		Properties camProps = props.newProps("camera");
		camProps.put("pitch", camera.pitch);
		camProps.put("yaw", camera.yaw);
	}

	@Override
	public void load(Properties props) {
		inventory.load(props.getProps("inventory"));
		hotbar.load(props.getProps("hotbar"));
		player.load(props.getProps("player"));
		
		Properties camProps = props.getProps("camera");
		camera.pitch = camProps.got("pitch");
		camera.yaw = camProps.got("yaw");
	}
}
