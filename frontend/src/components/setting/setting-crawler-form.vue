<template>
  <v-form>
    <v-card-content>
      <v-text-field
          v-model="setting.name"
          type="text"
          label="Название"
          :error-messages="(v.setting.name.$dirty && v.setting.name.$invalid && setting.name) ? v.setting.name.$silentErrors[0].$message : ''"
      />
      <v-text-field
          v-model="setting.referrer"
          type="text"
          label="Referrer"
          :error-messages="(v.setting.referrer.$dirty && v.setting.referrer.$invalid && setting.referrer) ? v.setting.referrer.$silentErrors[0].$message : ''"
      />
      <v-text-field
          v-model="setting.userAgent"
          type="text"
          label="User agent"
          :error-messages="(v.setting.userAgent.$dirty && v.setting.userAgent.$invalid && setting.userAgent) ? v.setting.userAgent.$silentErrors[0].$message : ''"
      />
      <v-text-field
          v-model="setting.delayInMs"
          type="text"
          label="Задержка в мс"
          :error-messages="(v.setting.delayInMs.$dirty && v.setting.delayInMs.$invalid && setting.delayInMs) ? v.setting.delayInMs.$silentErrors[0].$message : ''"
      />
      <v-text-field
          v-model="setting.reconnect"
          type="text"
          label="Кол-во переподключений"
          :error-messages="(v.setting.reconnect.$dirty && v.setting.reconnect.$invalid && setting.reconnect) ? v.setting.reconnect.$silentErrors[0].$message : ''"
      />
      <v-text-field
          v-model="setting.timeOutInMs"
          type="text"
          label="Время ожидания ответа в мс"
          :error-messages="(v.setting.timeOutInMs.$dirty && v.setting.timeOutInMs.$invalid && setting.timeOutInMs) ? v.setting.timeOutInMs.$silentErrors[0].$message : ''"
      />
    </v-card-content>

    <v-divider class="mb-4"/>

    <v-card-actions>
      <v-row>
        <v-col
            class="d-flex justify-center"
            cols="12">
          <v-btn
              :disabled="v.$invalid"
              @click="$emit('save', this.setting)"
          >
            Сохранить
          </v-btn>
        </v-col>
      </v-row>
    </v-card-actions>
  </v-form>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import useVuelidate from "@vuelidate/core";
import {minLength, maxLength, required, minValue, numeric} from "@vuelidate/validators";

export default defineComponent({
  name: "setting-crawler-form",

  setup() {
    return {
      v: useVuelidate()
    }
  },

  data() {
    return {
      setting: {
        name: '',
        referrer: '',
        userAgent: '',
        delayInMs: 1,
        reconnect: 1,
        timeOutInMs: 1,
      }
    }
  },

  validations() {
    return {
      setting: {
        name: {
          $autoDirty: true,
          required: required,
          minLength: minLength(3),
          maxLength: maxLength(32)
        },
        referrer: {
          $autoDirty: true,
          required: required,
          minLength: minLength(3),
          maxLength: maxLength(64)
        },
        userAgent: {
          $autoDirty: true,
          required: required,
          minLength: minLength(3),
          maxLength: maxLength(256)
        },
        delayInMs: {
          $autoDirty: true,
          required: required,
          numeric: numeric,
          minValue: minValue(1)
        },
        reconnect: {
          $autoDirty: true,
          required: required,
          numeric: numeric,
          minValue: minValue(1)
        },
        timeOutInMs: {
          $autoDirty: true,
          required: required,
          numeric: numeric,
          minValue: minValue(1)
        },
      }
    }
  },

  methods: {},
})
</script>

<style scoped>

</style>