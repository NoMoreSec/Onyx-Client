package net.onyx.client.items.impl.string;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.onyx.client.items.StringCreativeItem;
import net.onyx.client.utils.ClientUtils;
import org.apache.commons.lang3.StringUtils;

public class BanBook implements StringCreativeItem {

    @Override
    public String getNbtString() {
        String page = "'{\"text\":\"ⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫ\"}'";
        
        String[] pages = new String[100];
        for (int i = 0; i < 100; i++) {
            pages[i] = page;
        }

        return String.format("{author:\"%s\",pages:[%s],title:\"Bye-bye Book\"}", ClientUtils.getUsername(), StringUtils.join(pages, ","));
    }

    @Override
    public Item getItem() {
        return Items.WRITTEN_BOOK;
    }
    
}
