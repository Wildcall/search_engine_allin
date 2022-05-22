<template>
  <v-hover
      v-slot="{ isHovering, props }"
  >
    <v-card
        class="pa-4 bg-grey-lighten-3"
        :elevation="isHovering ? 20 : 5"
        v-bind="props"
        @click="openTasks"
    >
      <v-row>

        <v-col cols="1"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.id }}</h4>
        </v-col>

        <v-col cols="2"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.name }}</h4>
        </v-col>

        <v-col cols="3"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.path }}</h4>
        </v-col>

        <v-col cols="3"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.lastStatusTime }}</h4>
        </v-col>

        <v-col cols="2"
               class="d-flex align-center"
        >
          <h4 class="text-blue-grey-darken-4">{{ site.status }}</h4>
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

      </v-row>
      <v-dialog
          v-model="confirmDeleteDialog"
          persistent
      >
        <site-delete-confirm
            :site="site"
            @delete="deleteAction"
            @close="confirmDeleteDialog = false"
        />
      </v-dialog>
      <v-dialog
          v-model="showTasks"
          persistent
      >
        <site-tasks
            :tasks="taskStore.getSiteTask(this.site.id)"
            @delete="deleteAction"
            @close="showTasks = false"
        />
      </v-dialog>
    </v-card>
  </v-hover>
</template>

<script lang="ts">
import {defineComponent, PropType} from "vue";
import {useSiteStore} from "@/store/SiteStore";
import SiteDeleteConfirm from "@/components/site/site-delete-confirm.vue";
import SiteTasks from "@/components/site/site-tasks.vue";
import {useTaskStore} from "@/store/TaskStore";
import {SiteResponse} from "@/models/response/SiteResponse";

export default defineComponent({
  name: "site-info",

  components: {
    SiteTasks,
    SiteDeleteConfirm
  },

  props: {
    site: Object as PropType<SiteResponse>
  },

  data() {
    return {
      confirmDeleteDialog: false,
      showTasks: false
    }
  },

  setup() {
    return {
      siteStore: useSiteStore(),
      taskStore: useTaskStore()
    }
  },

  methods: {
    async openTasks() {
      if (this.site)
        await this.taskStore.findAll()
            .finally(() => this.showTasks = true)
    },

    async deleteAction() {
      if (this.site)
        await this.siteStore.delete(this.site.id)
            .finally(() => this.confirmDeleteDialog = false)
    }
  },
})
</script>

<style scoped>

</style>