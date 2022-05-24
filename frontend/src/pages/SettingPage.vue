<template>
  <v-container>
    <v-item-group
        mandatory
        v-model="selected"
    >
      <v-row>

        <v-col cols="12">
          <h1 class="font-weight-bold text-blue-grey-darken-4">
            Settings
          </h1>
        </v-col>

        <v-col cols="4">
          <v-item
              v-slot="{ isSelected, toggle }"
              :value="CRAWLER"
          >
            <info-card
                @add="openNewSettingForm(CRAWLER)"
                :type="CRAWLER"
                :selected="isSelected"
                button
                :data="settingStore.countSettingsOfType(CRAWLER)"
                @click="toggle"
            />
          </v-item>
        </v-col>

        <v-col cols="4">
          <v-item
              v-slot="{ isSelected, toggle }"
              :value="INDEXER"
          >
            <info-card
                @add="openNewSettingForm(INDEXER)"
                :type="INDEXER"
                :selected="isSelected"
                button
                :data="settingStore.countSettingsOfType(INDEXER)"
                @click="toggle"
            />
          </v-item>
        </v-col>

        <v-col cols="4">
          <v-item
              v-slot="{ isSelected, toggle }"
              :value="SEARCHER"
          >
            <info-card
                @add="openNewSettingForm(SEARCHER)"
                :type="SEARCHER"
                :selected="isSelected"
                button
                :data="settingStore.countSettingsOfType(SEARCHER)"
                @click="toggle"
            />
          </v-item>
        </v-col>

        <v-col cols="12"
               v-if="settingStore.getSettingsByType(selected).length===0"
        >
          <v-card
              class="pa-4 bg-grey-lighten-3"
          >
            <v-card-title>
              Настроек пока нет
            </v-card-title>
          </v-card>
        </v-col>

        <v-col cols="12"
               v-else
               v-for="setting in settingStore.getSettingsByType(selected)"
               :key="setting.id"
        >
          <setting-info
              :setting="setting"
          />
        </v-col>

      </v-row>
      <v-dialog
          v-model="newSettingForm"
          persistent
      >
        <setting-new
            :type="selected"
            @close="newSettingForm = false"
        />
      </v-dialog>
    </v-item-group>
  </v-container>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useSettingStore} from "@/store/SettingStore";
import InfoCard from "@/components/common/info-card.vue";
import {ResourceType} from "@/models/ResourceType";
import SettingInfo from "@/components/setting/setting-info.vue";
import SettingNew from "@/components/setting/setting-new.vue";
import {useSiteStore} from "@/store/SiteStore";
import {useTaskStore} from "@/store/TaskStore";

export default defineComponent({
  name: "SettingPage",

  components: {SettingNew, SettingInfo, InfoCard},

  setup() {
    return {
      CRAWLER: ResourceType.CRAWLER,
      INDEXER: ResourceType.INDEXER,
      SEARCHER: ResourceType.SEARCHER,
      siteStore: useSiteStore(),
      taskStore: useTaskStore(),
      settingStore: useSettingStore()
    }
  },

  data() {
    return {
      newSettingForm: false,
      selected: 'CRAWLER'
    }
  },

  methods: {
    openNewSettingForm(type: string) {
      this.selected = type
      this.newSettingForm = true
    }
  },

  mounted() {
    this.siteStore.findAll()
    this.taskStore.findAll()
    this.settingStore.findAll()
  }
})
</script>

