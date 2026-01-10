package com.flechazo.contact.client.renderer;

import cc.sighs.oelib.platform.Platform;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.entity.PostcardEntity;
import com.flechazo.contact.data.PostcardDataManager;
import com.flechazo.contact.data.PostcardStyle;
import com.flechazo.contact.helper.ColorHelper;
import com.flechazo.contact.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PostcardEntityRenderer<T extends PostcardEntity> extends EntityRenderer<T> {
    private static final ModelResourceLocation PIN_NEO = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "block/postcard_pin"), "standalone");
    private static final ModelResourceLocation POSTCARD_NEO = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "block/postcard"), "standalone");

    private static final ModelResourceLocation PIN_FA = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "block/postcard_pin"), "fabric_resource");
    private static final ModelResourceLocation POSTCARD_FA = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Contact.MOD_ID, "block/postcard"), "fabric_resource");
    private final BlockRenderDispatcher blockRenderDispatcher;
    private final List<String> list = Lists.newArrayList();
    private int textHash = 0;

    public PostcardEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        super.render(entity, yaw, tickDelta, poseStack, multiBufferSource, light);
        poseStack.pushPose();
        var direction = entity.getDirection();
        var vec3d = this.getRenderOffset(entity, tickDelta);
        poseStack.translate(-vec3d.x(), -vec3d.y(), -vec3d.z());
        double d = 0.46875;
        poseStack.translate((double) direction.getStepX() * d, (double) direction.getStepY() * d, (double) direction.getStepZ() * d);
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - entity.getYRot()));
        boolean bl = entity.isInvisible();
        var postcard = entity.getPostcard();
        if (!postcard.isEmpty()) {
            PostcardStyle postcardStyle;
            var styleId = postcard.get(ContactDataComponents.POSTCARD_STYLE_ID.get());
            postcardStyle = PostcardDataManager.getPostcards().getOrDefault(styleId, PostcardStyle.DEFAULT);

            float width = postcardStyle.cardWidth() / 2.0f;
            float height = postcardStyle.cardHeight() / 2.0f;

            int j = entity.getRotation();
            if (direction.get3DDataValue() > 1) {
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) ((j + 1) % 3 - 1) * 360.0f / 16.0f));
            } else {
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) j * 360.0f / 16.0f));
            }

            var modelManager = this.blockRenderDispatcher.getBlockModelShaper().getModelManager();
            if (!bl && direction.get3DDataValue() > 1) {
                float red = ColorHelper.getRedF(postcardStyle.postmarkColor());
                float green = ColorHelper.getGreenF(postcardStyle.postmarkColor());
                float blue = ColorHelper.getBlueF(postcardStyle.postmarkColor());
                poseStack.pushPose();
                poseStack.translate(-0.5f, -0.5f, -0.5f);
                poseStack.translate(0.0f, -(128 - height) / 256.0f, 0.0f);
                if (Platform.isFabric()) {
                    this.blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(Sheets.solidBlockSheet()), null, modelManager.getModel(PIN_FA), red, green, blue, light, OverlayTexture.NO_OVERLAY);
                }
                if (Platform.isNeoForge()) {
                    this.blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(Sheets.solidBlockSheet()), null, modelManager.getModel(PIN_NEO), red, green, blue, light, OverlayTexture.NO_OVERLAY);
                }
                poseStack.popPose();
            }

            poseStack.pushPose();
            poseStack.scale(width / 128.0f, height / 128.0f, 1.0f);
            poseStack.translate(-0.5f, -0.5f, -0.5f);
            if (Platform.isFabric()) {
                this.blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(Sheets.solidBlockSheet()), null, modelManager.getModel(POSTCARD_FA), 1.0f, 1.0f, 1.0f, light, OverlayTexture.NO_OVERLAY);
            }
            if (Platform.isNeoForge()) {
                this.blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(Sheets.solidBlockSheet()), null, modelManager.getModel(POSTCARD_NEO), 1.0f, 1.0f, 1.0f, light, OverlayTexture.NO_OVERLAY);
            }
            poseStack.popPose();

            poseStack.translate(0.0f, 0.0f, 0.5f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
            poseStack.scale(0.0078125f, 0.0078125f, 0.0078125f);
            poseStack.translate(-64.0f, -64.0f, 0.0f);
            poseStack.translate(0.0f, 0.0f, -1.0f);

            var matrix4f = poseStack.last().pose();
            var vertexConsumer = multiBufferSource.getBuffer(ClientUtil.getPostcardCardRenderLayer(postcardStyle));

            float pointX0 = 64.0f - width / 2.0f;
            float pointX1 = 64.0f + width / 2.0f;
            float pointY0 = 64.0f - height / 2.0f;
            float pointY1 = 64.0f + height / 2.0f;

            vertexConsumer.addVertex(matrix4f, pointX0, pointY1, -0.01f).setColor(255, 255, 255, 255).setUv(0.0f, 1.0f).setLight(light);
            vertexConsumer.addVertex(matrix4f, pointX1, pointY1, -0.01f).setColor(255, 255, 255, 255).setUv(1.0f, 1.0f).setLight(light);
            vertexConsumer.addVertex(matrix4f, pointX1, pointY0, -0.01f).setColor(255, 255, 255, 255).setUv(1.0f, 0.0f).setLight(light);
            vertexConsumer.addVertex(matrix4f, pointX0, pointY0, -0.01f).setColor(255, 255, 255, 255).setUv(0.0f, 0.0f).setLight(light);

            // 检查是否有发送者信息
            var sender = postcard.get(ContactDataComponents.POSTCARD_SENDER.get());
            if (sender != null && !sender.isEmpty()) {
                float markX0 = pointX0 + postcardStyle.postmarkPosX() / 2.0f;
                float markY0 = pointY0 + postcardStyle.postmarkPosY() / 2.0f;

                var vertex = multiBufferSource.getBuffer(ClientUtil.getPostcardPostmarkRenderLayer(postcardStyle));

                float markWidth = postcardStyle.postmarkWidth() / 2.0f;
                float markHeight = postcardStyle.postmarkHeight() / 2.0f;

                int red = ColorHelper.getRed(postcardStyle.postmarkColor());
                int green = ColorHelper.getGreen(postcardStyle.postmarkColor());
                int blue = ColorHelper.getBlue(postcardStyle.postmarkColor());
                int alpha = ColorHelper.getAlpha(postcardStyle.postmarkColor());

                vertex.addVertex(matrix4f, markX0, markY0 + markHeight, -0.02f).setColor(red, green, blue, alpha).setUv(0.0f, 1.0f).setLight(light);
                vertex.addVertex(matrix4f, markX0 + markWidth, markY0 + markHeight, -0.02f).setColor(red, green, blue, alpha).setUv(1.0f, 1.0f).setLight(light);
                vertex.addVertex(matrix4f, markX0 + markWidth, markY0, -0.02f).setColor(red, green, blue, alpha).setUv(1.0f, 0.0f).setLight(light);
                vertex.addVertex(matrix4f, markX0, markY0, -0.02f).setColor(red, green, blue, alpha).setUv(0.0f, 0.0f).setLight(light);
            }

            // 获取明信片文本
            var text = postcard.get(ContactDataComponents.POSTCARD_TEXT.get());
            if (text != null && !text.isBlank()) {
                var font = Minecraft.getInstance().font;

                float textX0 = pointX0 + postcardStyle.textPosX() / 2.0f;
                float textY0 = pointY0 + postcardStyle.textPosY() / 2.0f;

                if (textHash != text.hashCode()) {
                    list.clear();
                    font.getSplitter().splitLines(text, postcardStyle.textWidth(), Style.EMPTY, true, (style, lineStartPos, lineEndPos) ->
                    {
                        var lineTextRaw = text.substring(lineStartPos, lineEndPos);
                        var lineText = StringUtils.stripEnd(lineTextRaw, " \n");
                        list.add(lineText);
                    });
                    textHash = text.hashCode();
                }

                poseStack.pushPose();
                poseStack.translate(textX0, textY0, -0.025f);
                poseStack.scale(0.5f, 0.5f, 1.0f);
                poseStack.translate(0.0f, 0.0f, -0.1f);
                for (String t : list) {
                    font.drawInBatch(t, 0.0f, 0.0f, postcardStyle.textColor(), false, poseStack.last().pose(), multiBufferSource, Font.DisplayMode.NORMAL, 0, light);
                    poseStack.translate(0.0f, 12.0f, 0.0f);
                }
                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public Vec3 getRenderOffset(T entity, float f) {
        return new Vec3((float) entity.getDirection().getStepX() * 0.3f, -0.25, (float) entity.getDirection().getStepZ() * 0.3f);
    }

    @Override
    protected boolean shouldShowName(T entity) {
        if (!Minecraft.renderNames() || entity.getPostcard().isEmpty() || !entity.getPostcard().has(DataComponents.CUSTOM_NAME) || this.entityRenderDispatcher.crosshairPickEntity != entity) {
            return false;
        }
        double d = this.entityRenderDispatcher.distanceToSqr(entity);
        float f = entity.isDiscrete() ? 32.0f : 64.0f;
        return d < (double) (f * f);
    }
}