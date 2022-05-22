<template>
  <v-card
      :class="`bg-${style.line}`"
      elevation="5"
      class="pt-4 d-flex align-self-stretch"
  >
    <v-card :class="`bg-${style.bg}`"
            class="pa-4"
            flat
    >
      <v-card-title class="d-flex justify-center">
        <div :class="'font-weight-bold text-' + style.text">
          {{ task ? task.type : 'Добавить' }}
        </div>
      </v-card-title>
      <v-card-content v-if="task" class="d-flex justify-center">
        <div :class="'font-weight-bold text-' + style.text">
          {{ task?.taskState }}
        </div>
      </v-card-content>
      <v-card-actions class="justify-center">
        <div v-if="task"
             class="d-flex flex-nowrap"
        >
          <v-btn>
            Delete
          </v-btn>
          <v-btn>
            Edit
          </v-btn>
          <v-btn>
            Start
          </v-btn>
          <v-btn>
            Stop
          </v-btn>
        </div>
        <div v-else>
          <v-btn
              :color="style.text"
              icon="mdi-plus"
              size="small"
              @click="this.$emit('add')"
          />
        </div>
      </v-card-actions>
    </v-card>
  </v-card>
</template>

<script lang="ts">

import {defineComponent, PropType} from "vue";
import {TaskResponse} from "@/models/response/TaskResponse";
import {resourceStyle} from "@/style/ResourceStyle";
import {ResourceType} from "@/models/ResourceType";

export default defineComponent({
      name: 'task-card',

      data() {
        return {
          style: {
            text: '',
            line: '',
            bg: ''
          }
        }
      },

      props: {
        task: Object as PropType<TaskResponse>,
      },

      mounted() {
        if (this.task?.type)
          this.style = resourceStyle(this.task.type)
        else
          this.style = resourceStyle('')
      }
    }
)
</script>
