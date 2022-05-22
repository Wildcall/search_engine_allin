<template>
  <v-form>
    <v-card-content>
      <v-text-field
          v-model="setting.name"
          type="text"
          label="Название"
          :error-messages="(v.setting.name.$dirty && v.setting.name.$invalid && setting.name) ? v.setting.name.$silentErrors[0].$message : ''"
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
import {minLength, maxLength, required} from "@vuelidate/validators";

export default defineComponent({
  name: "setting-searcher-form",

  setup() {
    return {
      v: useVuelidate()
    }
  },

  data() {
    return {
      setting: {
        name: '',
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
      }
    }
  },

  methods: {},
})
</script>

<style scoped>

</style>