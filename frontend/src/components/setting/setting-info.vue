<template>
  <v-hover
      v-slot="{ isHovering, props }"
  >
    <v-card
        class="pa-4 bg-grey-lighten-3 "
        :elevation="isHovering ? 20 : 5"
        v-bind="props"
    >
      <v-row>

        <v-col cols="1"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ setting.id }}</h4>
        </v-col>

        <v-col cols="5"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ setting.name }}</h4>
        </v-col>

        <v-col cols="5"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ setting.type }}</h4>
        </v-col>

        <v-col cols="1"
               class="d-flex align-center"
        >
          <v-btn
              size="x-small"
              text
              variant="plain"
              icon="mdi-close"
              @click="confirmDeleteDialog = true"
          />
        </v-col>

        <v-col cols="12" v-if="isHovering">
          <v-row
              v-for="item in Object.entries(setting.resourceSettingDto)"
              :key="item"
          >
            <v-col cols="2">
              <h4>{{ item[0] }}</h4>
            </v-col>
            <v-col cols="10">
              {{ item[1] }}
            </v-col>
          </v-row>
        </v-col>

      </v-row>
    </v-card>
    <v-dialog
        v-model="showDeleteDialog"
        persistent
    >
      <setting-delete-confirm
          :setting="setting"
          @close="showDeleteDialog = false"
          @delete="deleteAction"
      />
    </v-dialog>
  </v-hover>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useSettingStore} from "@/store/SettingStore";
import SettingDeleteConfirm from "@/components/setting/setting-delete-confirm.vue";

export default defineComponent({
  name: "setting-info",

  components: {SettingDeleteConfirm},

  props: {
    setting: {}
  },

  data() {
    return {
      showDeleteDialog: false,
      editDialog: false
    }
  },

  setup() {
    return {
      settingStore: useSettingStore()
    }
  },

  methods: {
    async deleteAction() {
      await this.settingStore.delete((this.setting as any).id)
          .finally(() => this.showDeleteDialog = false)
    },
  }
})
</script>

<style scoped>

</style>