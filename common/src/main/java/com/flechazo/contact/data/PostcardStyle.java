package com.flechazo.contact.data;

import cc.sighs.oelib.data.api.DataDriven;
import com.flechazo.contact.Contact;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@DataDriven(
        modid = Contact.MOD_ID,
        folder = "postcards",
        syncToClient = true,
        priority = 100
)
public record PostcardStyle(
        PostcardInfo postcard,
        TradeInfo trade,
        TextInfo text,
        PostmarkInfo postmark
) {

    public static final Codec<PostcardStyle> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PostcardInfo.CODEC.fieldOf("postcard").forGetter(PostcardStyle::postcard),
                    TradeInfo.CODEC.fieldOf("trade").forGetter(PostcardStyle::trade),
                    TextInfo.CODEC.fieldOf("text").forGetter(PostcardStyle::text),
                    PostmarkInfo.CODEC.fieldOf("postmark").forGetter(PostcardStyle::postmark)
            ).apply(instance, PostcardStyle::new)
    );
    public static final PostcardStyle DEFAULT = new PostcardStyle(
            new PostcardInfo("contact:stripes", 200, 133),
            new TradeInfo(true, new ItemStack(Items.EMERALD)),
            new TextInfo(10, 12, 180, 96, new ColorInfo(255, 183, 111, 64)),
            new PostmarkInfo("contact:postmark", 142, -5, 64, 52, new ColorInfo(120, 182, 153, 104))
    );

    public static PostcardStyle fromNBT(CompoundTag tag) {
        if (tag.contains("Info")) {
            CompoundTag info = tag.getCompound("Info");
            String id = "contact:" + info.getString("ID");
            int posX = info.getInt("PosX");
            int posY = info.getInt("PosY");
            int textWidth = info.getInt("Width");
            int textHeight = info.getInt("Height");
            int color = info.getInt("Color");
            return new PostcardStyle(
                    new PostcardInfo(id, 200, 133),
                    new TradeInfo(true, new ItemStack(Items.EMERALD)),
                    new TextInfo(posX, posY, textWidth, textHeight,
                            new ColorInfo((color >> 24) & 0xFF, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF)),
                    new PostmarkInfo("contact:postmark", 142, -5, 64, 52,
                            new ColorInfo((color >> 24) & 0xCD, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF))
            );
        }

        if (tag.contains("CardID")) {
            ResourceLocation cardID = ResourceLocation.parse(tag.getString("CardID"));
            return PostcardDataManager.getPostcards().getOrDefault(cardID, DEFAULT);
        }

        return CODEC.parse(NbtOps.INSTANCE, tag)
                .result()
                .orElse(DEFAULT);
    }

    public static PostcardStyle fromItemStack(ItemStack itemStack) {
        ResourceLocation styleId = itemStack.get(ContactDataComponents.POSTCARD_STYLE_ID.get());
        if (styleId != null) {
            return PostcardDataManager.getPostcards().getOrDefault(styleId, DEFAULT);
        }
        return DEFAULT;
    }

    public String cardTexture() {
        return postcard.texture();
    }

    public int cardWidth() {
        return postcard.width();
    }

    public int cardHeight() {
        return postcard.height();
    }

    public ItemStack cardPrice() {
        return trade.price();
    }

    public boolean soldByTrader() {
        return trade.soldByTrader();
    }

    public int textPosX() {
        return text.x();
    }

    public int textPosY() {
        return text.y();
    }

    public int textWidth() {
        return text.width();
    }

    public int textHeight() {
        return text.height();
    }

    public int textColor() {
        return text.color().toARGB();
    }

    public String postmarkTexture() {
        return postmark.texture();
    }

    public int postmarkPosX() {
        return postmark.x();
    }

    public int postmarkPosY() {
        return postmark.y();
    }

    public int postmarkWidth() {
        return postmark.width();
    }

    public int postmarkHeight() {
        return postmark.height();
    }

    public int postmarkColor() {
        return postmark.color().toARGB();
    }

    public ResourceLocation getCardTexture() {
        ResourceLocation origin = ResourceLocation.parse(postcard.texture());
        return ResourceLocation.fromNamespaceAndPath(origin.getNamespace(), "textures/postcard/" + origin.getPath() + ".png");
    }

    public ResourceLocation getPostmarkTexture() {
        ResourceLocation origin = ResourceLocation.parse(postmark.texture());
        return ResourceLocation.fromNamespaceAndPath(origin.getNamespace(), "textures/postcard/" + origin.getPath() + ".png");
    }

    public record PostcardInfo(
            String texture,
            int width,
            int height
    ) {
        public static final Codec<PostcardInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("texture").forGetter(PostcardInfo::texture),
                        Codec.INT.fieldOf("width").forGetter(PostcardInfo::width),
                        Codec.INT.fieldOf("height").forGetter(PostcardInfo::height)
                ).apply(instance, PostcardInfo::new)
        );
    }

    public record TradeInfo(
            boolean soldByTrader,
            ItemStack price
    ) {
        public static final Codec<TradeInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.BOOL.optionalFieldOf("soldByTrader", true).forGetter(TradeInfo::soldByTrader),
                        ItemStack.CODEC.fieldOf("price").forGetter(TradeInfo::price)
                ).apply(instance, TradeInfo::new)
        );
    }

    public record ColorInfo(
            int alpha,
            int red,
            int green,
            int blue
    ) {
        public static final Codec<ColorInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("alpha").forGetter(ColorInfo::alpha),
                        Codec.INT.fieldOf("red").forGetter(ColorInfo::red),
                        Codec.INT.fieldOf("green").forGetter(ColorInfo::green),
                        Codec.INT.fieldOf("blue").forGetter(ColorInfo::blue)
                ).apply(instance, ColorInfo::new)
        );

        public int toARGB() {
            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }

    public record TextInfo(
            int x,
            int y,
            int width,
            int height,
            ColorInfo color
    ) {
        public static final Codec<TextInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("x").forGetter(TextInfo::x),
                        Codec.INT.fieldOf("y").forGetter(TextInfo::y),
                        Codec.INT.fieldOf("width").forGetter(TextInfo::width),
                        Codec.INT.fieldOf("height").forGetter(TextInfo::height),
                        ColorInfo.CODEC.fieldOf("color").forGetter(TextInfo::color)
                ).apply(instance, TextInfo::new)
        );
    }

    public record PostmarkInfo(
            String texture,
            int x,
            int y,
            int width,
            int height,
            ColorInfo color
    ) {
        public static final Codec<PostmarkInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("texture").forGetter(PostmarkInfo::texture),
                        Codec.INT.fieldOf("x").forGetter(PostmarkInfo::x),
                        Codec.INT.fieldOf("y").forGetter(PostmarkInfo::y),
                        Codec.INT.fieldOf("width").forGetter(PostmarkInfo::width),
                        Codec.INT.fieldOf("height").forGetter(PostmarkInfo::height),
                        ColorInfo.CODEC.fieldOf("color").forGetter(PostmarkInfo::color)
                ).apply(instance, PostmarkInfo::new)
        );
    }
}