package com.flechazo.contact.client.overlay;

import com.flechazo.contact.client.shader.ContactShaderResources;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import cc.sighs.oelib.bless.render.AbstractShaderOverlay;

@Environment(EnvType.CLIENT)
public class RedPacketOverlay extends AbstractShaderOverlay {
    public static final RedPacketOverlay INSTANCE = new RedPacketOverlay();
    public static final String FESTIVAL_ID = "red_packet";
    public static final String FESTIVAL_NAME = "红包祝福";

    private Component[] cachedLines = new Component[]{
            Component.literal("你收到了红包"),
            Component.literal("恭喜发财")
    };

    private RedPacketOverlay() {
        super(6000L, 600L, 400L);
    }

    public void showWithBlessing(String blessing, String sender) {
        if (blessing == null) blessing = "";
        Component title = Component.literal("你收到了红包");
        Component line1 = blessing.isEmpty()
                ? Component.literal("恭喜发财")
                : Component.literal(blessing);
        if (sender != null && !sender.isEmpty()) {
            Component line2 = Component.literal("来自: " + sender);
            this.cachedLines = new Component[]{title, line1, line2};
        } else {
            this.cachedLines = new Component[]{title, line1};
        }
        this.show();
    }

    @Override
    protected ShaderInstance getShader() {
        return ContactShaderResources.getRedPacketShader();
    }

    @Override
    protected int overlayWidth() {
        return 192;
    }

    @Override
    protected int overlayHeight() {
        return 64;
    }

    @Override
    protected void onShow(Minecraft minecraft) {
    }

    @Override
    protected void onHide(Minecraft minecraft) {
    }

    @Override
    protected void applyUniforms(ShaderInstance shader, float timeSeconds, float alpha, int overlayWidth, int overlayHeight, int mouseX, int mouseY, int screenWidth, int screenHeight) {
        shader.safeGetUniform("Time").set(timeSeconds);
        shader.safeGetUniform("Resolution").set((float) overlayWidth, (float) overlayHeight);
        shader.safeGetUniform("ToastAlpha").set(alpha);
    }

    @Override
    protected Component[] textLines(Minecraft minecraft) {
        return cachedLines;
    }

    @Override
    public boolean isChineseFestival() {
        return false;
    }

    @Override
    public String festivalId() {
        return FESTIVAL_ID;
    }

    @Override
    public String festivalName() {
        return FESTIVAL_NAME;
    }
}
