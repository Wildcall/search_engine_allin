<template>
  <v-card
      v-if="settings && siteId && type"
      class="bg-grey-lighten-3"
      style="width: 500px"
      elevation="5"
  >
    <v-card-title class="justify-center bg-blue-grey-darken-4">
      <div class="ml-4 text-teal-lighten-5">
        Добавить задачу {{ type }}
      </div>
      <v-spacer/>
      <div>
        <v-btn
            color="teal-lighten-5"
            icon="mdi-close"
            variant="text"
            @click="this.close"
        />
      </div>
    </v-card-title>

    <div class="pa-4">
      <v-card-content>
        <v-form>
          <v-radio-group
              v-model="task.settingId"
              label="Выберите настроку:"
          >
            <v-radio
                v-for="setting in settings"
                :key="setting.id"
                :label="`${setting.name}`"
                :value="setting.id"
            >
            </v-radio>
          </v-radio-group>
          <v-switch
              v-model="task.sendNotification"
              label="Отправить уведомление об окончании на почту?"
              color="green-darken-1"
          />
          <v-switch
              v-model="task.autoContinue"
              color="green-darken-1"
              label="Автоматический запуск следующего задания?"
          />
          <v-text-field
              v-model="task.eventFreqInMs"
              type="text"
              label="Частота уведомлений в мс"
              density="compact"
              variant="outlined"
              :error-messages="(v.task.eventFreqInMs.$dirty && v.task.eventFreqInMs.$invalid && task.eventFreqInMs) ? v.task.eventFreqInMs.$silentErrors[0].$message : ''"
          />
        </v-form>
      </v-card-content>

      <v-divider class="mb-4"/>

      <v-card-actions class="d-flex justify-center">
        <v-btn
            :disabled="v.$invalid"
            text
            class="font-weight-bold"
            @click="this.save"
        >
          Сохранить
        </v-btn>
      </v-card-actions>
    </div>
  </v-card>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import useVuelidate from "@vuelidate/core";
import {required, minValue, numeric} from "@vuelidate/validators";
import {useTaskStore} from "@/store/TaskStore";
import {useSettingStore} from "@/store/SettingStore";
import {SettingResponse} from "@/models/response/SettingResponse";

export default defineComponent({
  name: "task-new",

  setup() {
    return {
      v: useVuelidate(),
      settingStore: useSettingStore()
    }
  },

  data() {
    return {
      task: {
        siteId: this.siteId as number,
        settingId: -1,
        sendNotification: false,
        autoContinue: false,
        eventFreqInMs: 1000,
      },
      settings: {} as SettingResponse[]
    }
  },

  props: {
    type: String,
    siteId: Number
  },

  validations() {
    return {
      task: {
        settingId: {
          $autoDirty: true,
          numeric: numeric,
          required: required
        },
        sendNotification: {
          $autoDirty: true,
          required: required,
        },
        autoContinue: {
          $autoDirty: true,
          required: required,
        },
        eventFreqInMs: {
          $autoDirty: true,
          required: required,
          numeric: numeric,
          minValue: minValue(1000)
        }
      }
    }
  },

  methods: {
    async save() {
      if (!await this.v.$validate()) return
      this.$emit('save-task', this.task)
    },

    close() {
      this.reset()
      this.$emit('close')
    },

    reset() {
      this.task.siteId = -1
      this.task.settingId = -1
      this.task.sendNotification = false
      this.task.autoContinue = false
      this.task.eventFreqInMs = 1000
    }
  },

  mounted() {
    this.settings = this.settingStore.getSettingsByType(this.type as string)
  }
})
</script>

<style scoped>

</style>