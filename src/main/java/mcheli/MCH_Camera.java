package mcheli;

import javax.annotation.Nullable;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_EntityRenderer;
import mcheli.wrapper.W_Lib;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class MCH_Camera {
    private final Level level;
    private float zoom;
    private int[] mode;
    private boolean[] canUseShader;
    private int[] lastMode;
    public double posX;
    public double posY;
    public double posZ;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    private int lastZoomDir;
    public float partRotationYaw;
    public float partRotationPitch;
    public float prevPartRotationYaw;
    public float prevPartRotationPitch;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_NIGHTVISION = 1;
    public static final int MODE_THERMALVISION = 2;

    public MCH_Camera(Level level, Entity entity) {
        this.level = level;
        this.mode = new int[]{0, 0};
        this.zoom = 1.0f;
        this.lastMode = new int[this.getUserMax()];
        this.lastZoomDir = 0;
        this.canUseShader = new boolean[this.getUserMax()];
    }

    public MCH_Camera(Level level, Entity entity, double x, double y, double z) {
        this(level, entity);
        this.setPosition(x, y, z);
        this.setCameraZoom(1.0f);
    }

    public int getUserMax() {
        return this.mode.length;
    }

    public void initCamera(int uid, @Nullable Entity viewer) {
        this.setCameraZoom(1.0f);
        this.setMode(uid, 0);
        this.updateViewer(uid, viewer);
    }

    public void setMode(int uid, int m) {
        if (!this.isValidUid(uid)) {
            return;
        }
        this.mode[uid] = m < 0 ? 0 : m % this.getModeNum(uid);
        switch (this.mode[uid]) {
            case 2: {
                if (!this.level.isClientSide()) break;
                W_EntityRenderer.activateShader("pencil");
                break;
            }
            case 0:
            case 1: {
                if (!this.level.isClientSide()) break;
                W_EntityRenderer.deactivateShader();
            }
        }
    }

    public void setShaderSupport(int uid, Boolean b) {
        if (this.isValidUid(uid)) {
            this.setMode(uid, 0);
            this.canUseShader[uid] = b;
        }
    }

    public boolean isValidUid(int uid) {
        return uid >= 0 && uid < this.getUserMax();
    }

    public int getModeNum(int uid) {
        if (!this.isValidUid(uid)) {
            return 2;
        }
        return this.canUseShader[uid] ? 3 : 2;
    }

    public int getMode(int uid) {
        return this.isValidUid(uid) ? this.mode[uid] : 0;
    }

    public String getModeName(int uid) {
        if (this.getMode(uid) == 1) {
            return "NIGHT VISION";
        }
        if (this.getMode(uid) == 2) {
            return "THERMAL VISION";
        }
        return "";
    }

    public void updateViewer(int uid, @Nullable Entity viewer) {
        if (!this.isValidUid(uid) || viewer == null) {
            return;
        }
        if (W_Lib.isEntityLivingBase(viewer) && !viewer.isRemoved()) {
            MobEffectInstance effect;
            if (this.getMode(uid) == 0 && this.lastMode[uid] != 0 && (effect = W_Entity.getActivePotionEffect(viewer, MobEffects.NIGHT_VISION)) != null && effect.getDuration() > 0 && effect.getDuration() < 500) {
                if (viewer.level.isClientSide()) {
                    W_Entity.removePotionEffectClient(viewer, MobEffects.NIGHT_VISION);
                } else {
                    W_Entity.removePotionEffect(viewer, MobEffects.NIGHT_VISION);
                }
            }
            if ((this.getMode(uid) == 1 || this.getMode(uid) == 2) && viewer.level.isClientSide()) {
                W_Entity.addPotionEffect(viewer, new MobEffectInstance(MobEffects.NIGHT_VISION, 250, 0, true, false));
            }
        }
        this.lastMode[uid] = this.getMode(uid);
    }

    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public void setCameraZoom(float z) {
        float prevZoom = this.zoom;
        this.zoom = Math.max(z, 1.0f);
        this.lastZoomDir = Float.compare(this.zoom, prevZoom);
    }

    public float getCameraZoom() {
        return this.zoom;
    }

    public int getLastZoomDir() {
        return this.lastZoomDir;
    }
}
