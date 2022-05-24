<template>
  <v-container>
    <v-row>

      <v-col cols="12">
        <h1 class="font-weight-bold text-blue-grey-darken-4">
          Sites
        </h1>
      </v-col>

      <v-col cols="3">
        <info-card
            @add="openNewSiteDialog"
            type="TOTAL"
            button
            :data="this.siteStore.getSites.length"
        />
      </v-col>

      <v-col cols="3">
        <info-card
            type="COMPLETE"
            :data="siteStore.getCountSitesWithAllCompleteTasks"
        />
      </v-col>

      <v-col cols="3">
        <info-card
            type="PROGRESSING"
            :data="siteStore.getProcessingSitesCount"
        />
      </v-col>

      <v-col cols="3">
        <info-card
            type="INTERRUPT"
            :data="siteStore.getCountSitesWithInterruptTasks"
        />
      </v-col>

      <v-col cols="12"
             v-if="siteStore.getSites.length===0"
      >
        <v-card
            class="pa-4 bg-grey-lighten-3"
        >
          <v-card-title>
            Сайтов пока что нет
          </v-card-title>
        </v-card>
      </v-col>

      <v-col cols="12"
             v-else
             v-for="site in siteStore.getSites"
             :key="site.id"
      >
        <site-info
            :site="site"
            @click-row="openTasksDialog"
            @click-search="openSearchDialog"
            @click-delete="openDeleteDialog"
        />
      </v-col>

    </v-row>

    <!--New site dialog-->
    <v-dialog
        v-model="showNewSiteDialog"
        persistent
    >
      <site-new
          @close="closeNewSiteDialog"
      />
    </v-dialog>

    <!--Delete site confirm dialog-->
    <v-dialog
        v-model="showDeleteDialog"
        persistent
    >
      <site-delete-confirm
          :site="siteStore.getSite(selectedSiteId)"
          @close="closeDeleteDialog"
          @delete="deleteAction"
      />
    </v-dialog>

    <!--Site tasks dialog-->
    <v-dialog
        v-model="showTasksDialog"
        persistent
    >
      <site-tasks
          :tasks="taskStore.getSiteTask(selectedSiteId)"
          :site="siteStore.getSite(selectedSiteId)"
          @delete="deleteAction"
          @close="closeTasksDialog"
          @add="openNewTaskDialog"
      />
    </v-dialog>

    <!--New task dialog-->
    <v-dialog
        v-model="showNewTaskDialog"
        persistent
    >
      <task-new
          @close="closeNewTaskDialog"
          @save-task="saveTask"
          :type="selectedType"
          :site-id="selectedSiteId"
      />
    </v-dialog>

    <!--Search dialog-->
    <v-dialog
        v-model="showSearchDialog"
        persistent
    >
      <search-dialog
          :site-id="selectedSiteId"
          @close="closeSearchDialog"
      />
    </v-dialog>

  </v-container>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useSiteStore} from "@/store/SiteStore";
import SiteNew from "@/components/site/site-new.vue"
import SiteInfo from "@/components/site/site-info.vue";
import InfoCard from "@/components/common/info-card.vue";
import {useTaskStore} from "@/store/TaskStore";
import {useSettingStore} from "@/store/SettingStore";
import SearchDialog from "@/components/site/search-dialog.vue";
import SiteTasks from "@/components/site/site-tasks.vue";
import SiteDeleteConfirm from "@/components/site/site-delete-confirm.vue";
import TaskNew from "@/components/task/task-new.vue";
import {TaskAddRequest} from "@/models/request/TaskAddRequest";

export default defineComponent({
  name: "SitePage",

  components: {
    InfoCard,
    SiteNew,
    SiteInfo,
    SearchDialog,
    SiteTasks,
    SiteDeleteConfirm,
    TaskNew
  },

  setup() {
    return {
      siteStore: useSiteStore(),
      taskStore: useTaskStore(),
      setStore: useSettingStore()
    }
  },

  data() {
    return {
      showNewSiteDialog: false,
      showDeleteDialog: false,
      showTasksDialog: false,
      showNewTaskDialog: false,
      showSearchDialog: false,
      selectedType: '',
      selectedSiteId: -1
    }
  },

  methods: {
    openNewSiteDialog() {
      this.showNewSiteDialog = true
    },

    closeNewSiteDialog() {
      this.showNewSiteDialog = false
    },

    openDeleteDialog(id: number) {
      this.selectedSiteId = id
      this.showDeleteDialog = true
    },

    closeDeleteDialog() {
      this.selectedSiteId = -1
      this.showDeleteDialog = false
    },

    openTasksDialog(id: number) {
      if (this.showDeleteDialog || this.showSearchDialog) return
      this.selectedSiteId = id
      this.showTasksDialog = true
    },

    closeTasksDialog() {
      this.selectedSiteId = -1
      this.showTasksDialog = false
    },

    openNewTaskDialog(type: string) {
      this.showTasksDialog = false
      this.selectedType = type
      this.showNewTaskDialog = true
    },

    closeNewTaskDialog() {
      this.selectedSiteId = -1
      this.selectedType = ''
      this.showNewTaskDialog = false
    },

    openSearchDialog(id: number) {
      this.selectedSiteId = id
      this.showSearchDialog = true
    },

    closeSearchDialog() {
      this.selectedSiteId = -1
      this.showSearchDialog = false
    },

    async deleteAction() {
      if (this.selectedSiteId > 0)
        await this.siteStore.delete(this.selectedSiteId)
            .finally(() => this.showDeleteDialog = false)
    },

    async saveTask(task: TaskAddRequest) {
      await this.taskStore.add(task)
          .finally(() => this.closeNewTaskDialog())
    }
  },

  mounted() {
    this.siteStore.findAll()
    this.taskStore.findAll()
    this.setStore.findAll()
  }
})
</script>

<style scoped>

</style>