<template>
  <div class="d-flex flex-column elevation-4 rounded overflow-hidden">
    <div style="height: 16px; width: 250px"
         :class="`bg-${style.line}`"
    />
    <div :class="`bg-${style.bg}`"
         class="pa-2"
         style="max-width: 250px"
    >
      <div v-if="!task">
        <div class="d-flex justify-center font-weight-bold"
             :class="'text-' + style.text"
        >
          Добавить
        </div>
        <div class="d-flex justify-center mt-4">
          <v-btn
              :color="style.text"
              icon="mdi-plus"
              size="small"
              @click="this.$emit('add')"
          />
        </div>
      </div>
      <div v-else>
        <div class="d-flex justify-space-between">
          <div class="d-flex align-center font-weight-bold"
               :class="'text-' + style.text"
          >
            {{ task.type }}
          </div>
          <div class="d-flex align-center">
            <v-btn
                v-if="task"
                size="small"
                icon="mdi-close"
                variant="text"
                @click="deleteTask"
            />
          </div>
        </div>
        <div class="flex-column ma-2">
          <task-card-line
              :textStyle="style.text"
              label="Статус задачи"
              :value="task.taskState"
          />
          <task-card-line
              :textStyle="style.text"
              label="Создана"
              :value="task.createTime"
          />
          <task-card-line
              :textStyle="style.text"
              label="Время начала"
              :value="task.startTime"
          />
          <task-card-line
              :textStyle="style.text"
              label="Время окончания"
              :value="task.endTime"
          />
          <v-divider class="mt-4 mb-4"/>
          <div>
            <div class="text-caption text-medium-emphasis"
            >
              Уведомление на почту
            </div>
            <v-switch
                v-model="this.updatedTask.sendNotification"
                hide-details
                color="green-darken-1"
            >
              <template v-if="this.updatedTask.sendNotification !== this.task.sendNotification"
                  v-slot:label>
                <v-icon>mdi-content-save</v-icon>
              </template>
            </v-switch>
            <div class="text-caption text-medium-emphasis"
            >
              Автоматически продолжить
            </div>
            <v-switch
                v-model="this.updatedTask.autoContinue"
                hide-details
                color="green-darken-1"
            >
              <template v-if="this.updatedTask.autoContinue !== this.task.autoContinue"
                        v-slot:label>
                <v-icon>mdi-content-save</v-icon>
              </template>
            </v-switch>
            <div class="text-caption text-medium-emphasis"
            >
              Частота отправки логов в мс
            </div>
            <v-text-field
                v-model="this.updatedTask.eventFreqInMs"
                variant="outlined"
                density="compact"
                :append-inner-icon="this.updatedTask.eventFreqInMs !== this.task.eventFreqInMs ? 'mdi-content-save' : ''"
                :error-messages="(validate.updatedTask.eventFreqInMs.$dirty && validate.updatedTask.eventFreqInMs.$invalid && updatedTask.eventFreqInMs) ? validate.updatedTask.eventFreqInMs.$silentErrors[0].$message : ''"
            />
          </div>
        </div>
        <div class="d-flex justify-center flex-nowrap">
          <v-btn
              :color="`${style.text}`"
              @click="taskAction"
          >
            {{ task.taskState === 'START' ? 'Остановить' : 'Старт' }}
          </v-btn>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, PropType} from "vue";
import {TaskResponse} from "@/models/response/TaskResponse";
import {resourceStyle} from "@/style/ResourceStyle";
import {useTaskStore} from "@/store/TaskStore";
import TaskCardLine from "@/components/task/task-card-line.vue";
import {TaskStatus} from "@/models/TaskStatus";
import useVuelidate from "@vuelidate/core";
import {minValue, numeric, required} from "@vuelidate/validators";
import {TaskUpdateRequest} from "@/models/request/TaskUpdateRequest";

export default defineComponent({
  name: 'task-card',

  components: {TaskCardLine},

  setup() {
    return {
      validate: useVuelidate(),
      taskStore: useTaskStore()
    }
  },

  data() {
    return {
      style: {
        text: '',
        line: '',
        bg: ''
      },
      updatedTask: {
        id: this.task?.id,
        sendNotification: this.task?.sendNotification,
        autoContinue: this.task?.autoContinue,
        eventFreqInMs: this.task?.eventFreqInMs
      }
    }
  },

  validations() {
    return {
      updatedTask: {
        eventFreqInMs: {
          $autoDirty: true,
          required: required,
          minValue: minValue(1000),
          numeric: numeric
        }
      }
    }
  },

  props: {
    task: Object as PropType<TaskResponse>,
  },

  methods: {
    async deleteTask() {
      if (this.task?.id)
        await this.taskStore
            .delete(this.task.id)
            .then(() => {
              this.style = resourceStyle('')
            })
    },

    async taskAction() {
      if (!await this.validate.$validate()) return

      if (this.taskIsUpdated()) {
        await this.taskStore
            .update(this.updatedTask as TaskUpdateRequest)
            .then(() => {
              if (this.task?.taskState === TaskStatus.START) {
                this.taskStore.stop(this.task.id)
                return
              }
              if (this.task?.id)
                this.taskStore.start(this.task.id)
            })
        return
      }
      if (this.task?.taskState === TaskStatus.START) {
        await this.taskStore.stop(this.task.id)
        return
      }
      if (this.task?.id)
        await this.taskStore.start(this.task.id)
    },

    taskIsUpdated(): boolean {
      return this.updatedTask.eventFreqInMs !== this.task?.eventFreqInMs
          || this.updatedTask.sendNotification !== this.task?.sendNotification
          || this.updatedTask.autoContinue !== this.task?.autoContinue
    }
  },

  mounted() {
    if (this.task?.type)
      this.style = resourceStyle(this.task.type)
    else
      this.style = resourceStyle('')
  }
})
</script>
