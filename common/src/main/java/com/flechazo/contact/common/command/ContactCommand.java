package com.flechazo.contact.common.command;

import cc.sighs.oelib.data.DataManager;
import com.flechazo.contact.common.command.arguments.PostcardStyleArgument;
import com.flechazo.contact.common.component.ContactDataComponents;
import com.flechazo.contact.common.item.ParcelItem;
import com.flechazo.contact.common.item.PostcardItem;
import com.flechazo.contact.common.registry.ItemRegistry;
import com.flechazo.contact.common.storage.MailToBeSent;
import com.flechazo.contact.data.PostcardDataManager;
import com.flechazo.contact.platform.PlatformHelper;
import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ContactCommand {
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_PLAYERS = (context, builder) ->
    {
        var server = DataManager.getServer();
        Set<String> set = Sets.newHashSet();
        if (server != null) {
            set.addAll(PlatformHelper.getNameToUUID().keySet());
        }
        set.add("\"@e\"");
        return SharedSuggestionProvider.suggest(set, builder);
    };
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_POSTCARDS = (context, builder) ->
    {
        var collection = PostcardDataManager.getPostcards().keySet();
        return SharedSuggestionProvider.suggestResource(collection, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
                                CommandBuildContext access, Commands.CommandSelection environment) {
        dispatcher.register(
                Commands.literal("contact")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("postcard")
                                .then(Commands.literal("give")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(Commands.argument("postcard", ResourceLocationArgument.id())
                                                        .suggests(SUGGEST_POSTCARDS)
                                                        .then(Commands.argument("isEnderType", BoolArgumentType.bool())
                                                                .executes(context -> givePostcard(context.getSource(),
                                                                        PostcardStyleArgument.getPostcardStyleID(context, "postcard"),
                                                                        EntityArgument.getPlayers(context, "targets"),
                                                                        "",
                                                                        BoolArgumentType.getBool(context, "isEnderType"),
                                                                        "")
                                                                )
                                                                .then(Commands.argument("sender", StringArgumentType.string())
                                                                        .executes(context -> givePostcard(context.getSource(),
                                                                                PostcardStyleArgument.getPostcardStyleID(context, "postcard"),
                                                                                EntityArgument.getPlayers(context, "targets"),
                                                                                StringArgumentType.getString(context, "sender"),
                                                                                BoolArgumentType.getBool(context, "isEnderType"),
                                                                                StringArgumentType.getString(context, "text"))
                                                                        )
                                                                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                                                                .executes(context -> givePostcard(context.getSource(),
                                                                                        PostcardStyleArgument.getPostcardStyleID(context, "postcard"),
                                                                                        EntityArgument.getPlayers(context, "targets"),
                                                                                        StringArgumentType.getString(context, "sender"),
                                                                                        BoolArgumentType.getBool(context, "isEnderType"),
                                                                                        StringArgumentType.getString(context, "text"))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("deliver")
                                        .then(Commands.argument("targets", StringArgumentType.string())
                                                .suggests(SUGGEST_PLAYERS)
                                                .then(Commands.argument("postcard", ResourceLocationArgument.id())
                                                        .suggests(SUGGEST_POSTCARDS)
                                                        .then(Commands.argument("isEnderType", BoolArgumentType.bool())
                                                                .then(Commands.argument("sender", StringArgumentType.string())
                                                                        .then(Commands.argument("ticks", IntegerArgumentType.integer(0, 1728000))
                                                                                .executes(context -> deliverPostcard(context.getSource(),
                                                                                        PostcardStyleArgument.getPostcardStyleID(context, "postcard"),
                                                                                        StringArgumentType.getString(context, "targets"),
                                                                                        IntegerArgumentType.getInteger(context, "ticks"),
                                                                                        StringArgumentType.getString(context, "sender"),
                                                                                        BoolArgumentType.getBool(context, "isEnderType"),
                                                                                        "")
                                                                                )
                                                                                .then(Commands.argument("text", StringArgumentType.greedyString())
                                                                                        .executes(context -> deliverPostcard(context.getSource(),
                                                                                                PostcardStyleArgument.getPostcardStyleID(context, "postcard"),
                                                                                                StringArgumentType.getString(context, "targets"),
                                                                                                IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                StringArgumentType.getString(context, "sender"),
                                                                                                BoolArgumentType.getBool(context, "isEnderType"),
                                                                                                StringArgumentType.getString(context, "text"))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("parcel")
                                .then(Commands.literal("give")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(Commands.argument("isEnderType", BoolArgumentType.bool())
                                                        .then(Commands.argument("sender", StringArgumentType.string())
                                                                .then(Commands.argument("item1", ItemArgument.item(access))
                                                                        .then(Commands.argument("count1", IntegerArgumentType.integer(1, 64))
                                                                                .executes(context -> giveParcel(context.getSource(),
                                                                                        EntityArgument.getPlayers(context, "targets"),
                                                                                        StringArgumentType.getString(context, "sender"),
                                                                                        BoolArgumentType.getBool(context, "isEnderType"),
                                                                                        ItemArgument.getItem(context, "item1").createItemStack(IntegerArgumentType.getInteger(context, "count1"), false))
                                                                                )
                                                                                .then(Commands.argument("item2", ItemArgument.item(access))
                                                                                        .then(Commands.argument("count2", IntegerArgumentType.integer(1, 64))
                                                                                                .executes(context -> giveParcel(context.getSource(),
                                                                                                        EntityArgument.getPlayers(context, "targets"),
                                                                                                        StringArgumentType.getString(context, "sender"),
                                                                                                        BoolArgumentType.getBool(context, "isEnderType"),
                                                                                                        ItemArgument.getItem(context, "item1").createItemStack(IntegerArgumentType.getInteger(context, "count1"), false),
                                                                                                        ItemArgument.getItem(context, "item2").createItemStack(IntegerArgumentType.getInteger(context, "count2"), false))
                                                                                                )
                                                                                                .then(Commands.argument("item3", ItemArgument.item(access))
                                                                                                        .then(Commands.argument("count3", IntegerArgumentType.integer(1, 64))
                                                                                                                .executes(context -> giveParcel(context.getSource(),
                                                                                                                        EntityArgument.getPlayers(context, "targets"),
                                                                                                                        StringArgumentType.getString(context, "sender"),
                                                                                                                        BoolArgumentType.getBool(context, "isEnderType"),
                                                                                                                        ItemArgument.getItem(context, "item1").createItemStack(IntegerArgumentType.getInteger(context, "count1"), false),
                                                                                                                        ItemArgument.getItem(context, "item2").createItemStack(IntegerArgumentType.getInteger(context, "count2"), false),
                                                                                                                        ItemArgument.getItem(context, "item3").createItemStack(IntegerArgumentType.getInteger(context, "count3"), false))
                                                                                                                )
                                                                                                                .then(Commands.argument("item4", ItemArgument.item(access))
                                                                                                                        .then(Commands.argument("count4", IntegerArgumentType.integer(1, 64))
                                                                                                                                .executes(context -> giveParcel(context.getSource(),
                                                                                                                                        EntityArgument.getPlayers(context, "targets"),
                                                                                                                                        StringArgumentType.getString(context, "sender"),
                                                                                                                                        BoolArgumentType.getBool(context, "isEnderType"),
                                                                                                                                        ItemArgument.getItem(context, "item1").createItemStack(IntegerArgumentType.getInteger(context, "count1"), false),
                                                                                                                                        ItemArgument.getItem(context, "item2").createItemStack(IntegerArgumentType.getInteger(context, "count2"), false),
                                                                                                                                        ItemArgument.getItem(context, "item3").createItemStack(IntegerArgumentType.getInteger(context, "count3"), false),
                                                                                                                                        ItemArgument.getItem(context, "item4").createItemStack(IntegerArgumentType.getInteger(context, "count4"), false)))
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("deliver")
                                        .then(Commands.argument("targets", StringArgumentType.string())
                                                .suggests(SUGGEST_PLAYERS)
                                                .then(Commands.argument("isEnderType", BoolArgumentType.bool())
                                                        .then(Commands.argument("sender", StringArgumentType.string())
                                                                .then(Commands.argument("ticks", IntegerArgumentType.integer(0, 1728000))
                                                                        .then(Commands.argument("item1", ItemArgument.item(access))
                                                                                .then(Commands.argument("count1", IntegerArgumentType.integer(1, 64))
                                                                                        .executes(context -> deliverParcel(context.getSource(),
                                                                                                StringArgumentType.getString(context, "targets"),
                                                                                                IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                StringArgumentType.getString(context, "sender"),
                                                                                                BoolArgumentType.getBool(context, "isEnderType"),
                                                                                                ItemArgument.getItem(context, "item1").createItemStack(IntegerArgumentType.getInteger(context, "count1"), false))
                                                                                        )
                                                                                        .then(Commands.argument("item2", ItemArgument.item(access))
                                                                                                .then(Commands.argument("count2", IntegerArgumentType.integer(1, 64))
                                                                                                        .executes(context -> deliverParcel(context.getSource(),
                                                                                                                StringArgumentType.getString(context, "targets"),
                                                                                                                IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                                StringArgumentType.getString(context, "sender"),
                                                                                                                BoolArgumentType.getBool(context, "isEnderType"),
                                                                                                                ItemArgument.getItem(context, "item1").createItemStack(IntegerArgumentType.getInteger(context, "count1"), false),
                                                                                                                ItemArgument.getItem(context, "item2").createItemStack(IntegerArgumentType.getInteger(context, "count2"), false))
                                                                                                        )
                                                                                                        .then(Commands.argument("item3", ItemArgument.item(access))
                                                                                                                .then(Commands.argument("count3", IntegerArgumentType.integer(1, 64))
                                                                                                                        .executes(context -> deliverParcel(context.getSource(),
                                                                                                                                StringArgumentType.getString(context, "targets"),
                                                                                                                                IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                                                StringArgumentType.getString(context, "sender"),
                                                                                                                                BoolArgumentType.getBool(context, "isEnderType"),
                                                                                                                                ItemArgument.getItem(context, "item1").createItemStack(IntegerArgumentType.getInteger(context, "count1"), false),
                                                                                                                                ItemArgument.getItem(context, "item2").createItemStack(IntegerArgumentType.getInteger(context, "count2"), false),
                                                                                                                                ItemArgument.getItem(context, "item3").createItemStack(IntegerArgumentType.getInteger(context, "count3"), false))
                                                                                                                        )
                                                                                                                        .then(Commands.argument("item4", ItemArgument.item(access))
                                                                                                                                .then(Commands.argument("count4", IntegerArgumentType.integer(1, 64))
                                                                                                                                        .executes(context -> deliverParcel(context.getSource(),
                                                                                                                                                StringArgumentType.getString(context, "targets"),
                                                                                                                                                IntegerArgumentType.getInteger(context, "ticks"),
                                                                                                                                                StringArgumentType.getString(context, "sender"),
                                                                                                                                                BoolArgumentType.getBool(context, "isEnderType"),
                                                                                                                                                ItemArgument.getItem(context, "item1").createItemStack(IntegerArgumentType.getInteger(context, "count1"), false),
                                                                                                                                                ItemArgument.getItem(context, "item2").createItemStack(IntegerArgumentType.getInteger(context, "count2"), false),
                                                                                                                                                ItemArgument.getItem(context, "item3").createItemStack(IntegerArgumentType.getInteger(context, "count3"), false),
                                                                                                                                                ItemArgument.getItem(context, "item4").createItemStack(IntegerArgumentType.getInteger(context, "count4"), false)))
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static void deliverToPlayerMailbox(CommandSourceStack source, String target, int ticks, AtomicInteger n, ItemStack parcel) {
        var uuid = PlatformHelper.getNameToUUID().get(target);
        if (uuid != null) {
            if (!PlatformHelper.isMailboxFull(uuid)) {
                PlatformHelper.getMailList().add(new MailToBeSent(uuid, parcel, ticks));
                n.getAndIncrement();
            } else {
                source.sendSuccess(() -> Component.translatable("command.contact.deliver.full", target), true);
            }
        }
    }

    private static int deliverParcel(CommandSourceStack source, String target, int ticks, String sender, boolean isEnder, ItemStack... list) {
        AtomicInteger n = new AtomicInteger(0);
        SimpleContainer contents = new SimpleContainer(4);
        for (int i = 0; i < list.length; i++) {
            contents.setItem(i, list[i]);
        }
        var parcel = ParcelItem.getParcel(contents, isEnder, sender);
        if (target.equals("@e")) {
            PlatformHelper.getNameToUUID().keySet().forEach(name -> deliverToPlayerMailbox(source, name, ticks, n, parcel));
        } else {
            deliverToPlayerMailbox(source, target, ticks, n, parcel);
        }

        if (n.get() == 1 && target.equals("@e")) {
            source.sendSuccess(() -> Component.translatable("command.contact.deliver.success.single", new ItemStack(isEnder ? ItemRegistry.ENDER_PARCEL.get() : ItemRegistry.PARCEL.get()).getHoverName(), target), true);
        } else {
            source.sendSuccess(() -> Component.translatable("command.contact.deliver.success.multiple", new ItemStack(isEnder ? ItemRegistry.ENDER_PARCEL.get() : ItemRegistry.PARCEL.get()).getHoverName(), n.get()), true);
        }
        return n.get();
    }

    private static int giveParcel(CommandSourceStack source, Collection<ServerPlayer> targets, String sender, boolean isEnder, ItemStack... list) {
        SimpleContainer contents = new SimpleContainer(4);
        for (int i = 0; i < list.length; i++) {
            contents.setItem(i, list[i]);
        }
        var parcel = ParcelItem.getParcel(contents, isEnder, sender);

        giveParcelToPlayers(targets, parcel);

        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.give.success.single", 1, new ItemStack(isEnder ? ItemRegistry.ENDER_PARCEL.get() : ItemRegistry.PARCEL.get()).getHoverName(), targets.iterator().next().getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.give.success.single", 1, new ItemStack(isEnder ? ItemRegistry.ENDER_PARCEL.get() : ItemRegistry.PARCEL.get()).getHoverName(), targets.size()), true);
        }

        return targets.size();
    }

    private static int deliverPostcard(CommandSourceStack source, ResourceLocation id, String target, int ticks, String sender, boolean isEnder, String text) {
        text = text.replace("\\n", "\n");
        AtomicInteger n = new AtomicInteger(0);
        if (target.equals("@e")) {
            var postcard = PostcardItem.setText(PostcardItem.getPostcard(id, isEnder), text);
            postcard.set(ContactDataComponents.POSTCARD_SENDER.get(), sender);
            PlatformHelper.getNameToUUID().keySet().forEach(name -> deliverToPlayerMailbox(source, name, ticks, n, postcard));
        } else {
            var postcard = PostcardItem.setText(PostcardItem.getPostcard(id, false), text);
            postcard.set(ContactDataComponents.POSTCARD_SENDER.get(), sender);

            deliverToPlayerMailbox(source, target, ticks, n, postcard);
        }

        if (n.get() == 1 && !target.equals("@e")) {
            source.sendSuccess(() -> Component.translatable("command.contact.deliver.success.single", new ItemStack(isEnder ? ItemRegistry.ENDER_POSTCARD.get() : ItemRegistry.POSTCARD.get()).getHoverName(), target), true);
        } else {
            source.sendSuccess(() -> Component.translatable("command.contact.deliver.success.multiple", new ItemStack(isEnder ? ItemRegistry.ENDER_POSTCARD.get() : ItemRegistry.POSTCARD.get()).getHoverName(), n.get()), true);
        }
        return n.get();
    }

    private static int givePostcard(CommandSourceStack source, ResourceLocation postcardId, Collection<ServerPlayer> targets, String sender, boolean isEnderType, String text) {
        var postcard = PostcardItem.getPostcard(postcardId, isEnderType);
        if (!sender.isEmpty()) {
            postcard.set(ContactDataComponents.POSTCARD_SENDER.get(), sender);
        }
        if (!text.isEmpty()) {
            postcard.set(ContactDataComponents.POSTCARD_TEXT.get(), text);
        }

        AtomicInteger i = new AtomicInteger();
        targets.forEach(player -> {
            player.getInventory().placeItemBackInInventory(postcard.copy());
            i.getAndIncrement();
        });

        source.sendSuccess(() -> Component.translatable("commands.contact.postcard.give.success", i.get()), true);
        return i.get();
    }

    private static void giveParcelToPlayers(Collection<ServerPlayer> targets, ItemStack parcel) {
        for (ServerPlayer serverPlayer : targets) {
            boolean flag = serverPlayer.getInventory().add(parcel);
            if (flag) {
                var itemEntity = serverPlayer.drop(parcel, false);
                if (itemEntity != null) {
                    itemEntity.makeFakeItem();
                }

                serverPlayer.level().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((serverPlayer.getRandom().nextFloat() - serverPlayer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                serverPlayer.inventoryMenu.broadcastChanges();
            } else {
                var itementity = serverPlayer.drop(parcel, false);
                if (itementity != null) {
                    itementity.setNoPickUpDelay();
                    itementity.setThrower(serverPlayer);
                }
            }
        }
    }
}
