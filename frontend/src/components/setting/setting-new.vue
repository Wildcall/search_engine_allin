<template>
  <v-card
      class="bg-grey-lighten-3"
      style="width: 500px"
      elevation="5"
  >
    <v-card-title class="justify-center bg-blue-grey-darken-4">
      <div class="ml-4 text-teal-lighten-5">
        Добавить настройку
      </div>
      <v-spacer/>
      <div>
        <v-btn
            color="teal-lighten-5"
            icon="mdi-close"
            variant="text"
            @click="this.$emit('close')"
        />
      </div>
    </v-card-title>
    <div class="pa-4" >
      <v-tabs
          v-model="tab"
          class="mb-4"
      >
        <v-tab value="CRAWLER">
          CRAWLER
        </v-tab>
        <v-tab value="INDEXER">
          INDEXER
        </v-tab>
        <v-tab value="SEARCHER">
          SEARCHER
        </v-tab>
      </v-tabs>
      <v-window v-model="tab">
        <v-window-item value="CRAWLER">
          <setting-crawler-form
              @save="save"
          />
        </v-window-item>
        <v-window-item value="INDEXER">
          <setting-indexer-form
              @save="save"
          />
        </v-window-item>
        <v-window-item value="SEARCHER">
          <setting-searcher-form
              @save="save"
          />
        </v-window-item>
      </v-window>

    </div>
  </v-card>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {ResourceType} from "@/models/ResourceType";
import {useSettingStore} from "@/store/SettingStore";
import {ResourceSetting} from "@/models/setting/ResourceSetting";
import SettingCrawlerForm from "@/components/setting/setting-crawler-form.vue";
import SettingSearcherForm from "@/components/setting/setting-searcher-form.vue";
import SettingIndexerForm from "@/components/setting/setting-indexer-form.vue";

export default defineComponent({
  name: "setting-new",
  components: {SettingIndexerForm, SettingSearcherForm, SettingCrawlerForm},

  props: {
    type: String
  },

  setup() {
    return {
      settingStore: useSettingStore()
    }
  },

  data() {
    return {
      tab: {}
    }
  },

  methods: {
    async save(setting: ResourceSetting) {
      console.log()
      await this.settingStore.add(setting, this.type as ResourceType)
      this.close()
    },

    close() {
      this.$emit('close')
    }
  },

  mounted() {
    if (this.type !== undefined) this.tab = this.type
  }
})
</script>

<style scoped>

</style>