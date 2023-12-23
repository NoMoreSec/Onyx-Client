package net.onyx.client.modules.combat;

import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.modules.Module;

public class Reach extends Module {

   public Reach() {
      super("Reach");

      this.setDescription("makes your hands longer");
      this.setCategory("Combat");

      this.addSetting(new Setting("Block Reach", 5) {{
         this.setMin(0);
         this.setMax(6);
      }});

      this.addSetting(new Setting("Entity Reach", 5) {{
         this.setMin(0);
         this.setMax(6);
      }});
   }

   public float blockReach() {
      if (!this.isEnabled()) {
         return OnyxClient.getClient().interactionManager.getCurrentGameMode().isCreative() ? 5.0F : 4.5F;
      } else {
         return this.getIntSetting("Block Reach").floatValue();
      }
   }

   public float entityReach() {
      return !this.isEnabled() ? 3.0F : this.getIntSetting("Entity Reach").floatValue();
   }

   @Override
   public void fireEvent(Event event) {

   }

   @Override
   public void activate() {

   }

   @Override
   public void deactivate() {

   }
}