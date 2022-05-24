<template>
  <v-container fluid>
    <v-card
        v-if="tasks && site"
        class="bg-grey-lighten-3"
        elevation="5"
    >
      <v-card-title class="justify-center bg-blue-grey-darken-4">
        <div class="ml-4 text-teal-lighten-5">
          Задачи для сайта {{ site.path }}
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

      <div class="pa-4 d-flex">
        <task-card
            class="w-33 mr-4"
            :task="tasks[getTask('CRAWLER')]"
            @add="$emit('add', 'CRAWLER')"
        />
        <task-card
            class="w-33 mr-4"
            :task="tasks[getTask('INDEXER')]"
            @add="$emit('add', 'INDEXER')"
        />
        <task-card
            class="w-33"
            :task="tasks[getTask('SEARCHER')]"
            @add="$emit('add', 'SEARCHER')"
        />
      </div>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import {defineComponent, PropType} from "vue"
import TaskCard from "@/components/task/task-card.vue"
import {TaskResponse} from "@/models/response/TaskResponse";
import {SiteResponse} from "@/models/response/SiteResponse";

export default defineComponent({
  name: "site-tasks",

  components: {TaskCard},

  props: {
    tasks: Object as PropType<TaskResponse[]>,
    site: Object as PropType<SiteResponse>
  },

  methods: {
    getTask(type: string): number {
      if (this.tasks)
        return this.tasks?.findIndex(obj => obj.type === type)
      return -1
    },
  },
})
</script>

<style scoped>

</style>