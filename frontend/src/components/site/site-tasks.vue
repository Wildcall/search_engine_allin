<template>
  <v-container fluid>
    <v-card
        class="bg-grey-lighten-3"
        elevation="5"
    >
      <v-card-title class="justify-center bg-blue-grey-darken-4">
        <div class="ml-4 text-teal-lighten-5">
          Задачи
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

      <v-row class="pa-4">
        <v-col class="d-flex">
          <task-card
              :task="tasks[getTask('CRAWLER')]"
          />
        </v-col>
        <v-col class="d-flex">
          <task-card
              :task="tasks[getTask('INDEXER')]"
          />
        </v-col>
        <v-col class="d-flex">
          <task-card
              :task="tasks[getTask('SEARCHER')]"
          />
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import {defineComponent, PropType} from "vue"
import TaskCard from "@/components/common/task-card.vue"
import {TaskResponse} from "@/models/response/TaskResponse";

export default defineComponent({
  name: "site-tasks",

  components: {TaskCard},

  props: {
    tasks: Object as PropType<TaskResponse[]>
  },

  computed: {},

  methods: {
    getTask(type: string): number {
      if (this.tasks)
        return this.tasks?.findIndex(obj => obj.type === type)
      return -1
    }
  },
})
</script>

<style scoped>

</style>