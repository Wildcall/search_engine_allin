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
          v-model="setting.parallelism"
          type="text"
          label="Параллельность"
          :error-messages="(v.setting.parallelism.$dirty && v.setting.parallelism.$invalid && setting.parallelism) ? v.setting.parallelism.$silentErrors[0].$message : ''"
      />
      <v-row
          v-for="item in Object.entries(this.setting.selectorWeight)"
          :key="item"
      >
        <v-col cols="2">

        </v-col>
        <v-col cols="5">
          {{ item[0] }}
        </v-col>
        <v-col cols="5">
          {{ item[1] }}
        </v-col>
      </v-row>
      <selector-weight-form
          @add="addSelectorWeight"
      />
    </v-card-content>

    <v-divider class="mb-4"/>

    <v-card-actions>
      <v-row>
        <v-col
            class="d-flex justify-center"
            cols="12">
          <v-btn
              :disabled="v.setting.$invalid"
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
import {minLength, maxLength, required, minValue, maxValue} from "@vuelidate/validators";
import SelectorWeightForm from "@/components/setting/selector-weight-form.vue";

export default defineComponent({
  name: "setting-indexer-form",
  components: {SelectorWeightForm},
  setup() {
    return {
      v: useVuelidate()
    }
  },

  data() {
    return {
      setting: {
        name: '',
        selectorWeight: {},
        parallelism: 1,
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
        parallelism: {
          $autoDirty: true,
          required: required,
          minValue: minValue(1),
          maxValue: maxValue(8)
        },
        selectorWeight: {
          required: required,
        }
      }
    }
  },

  methods: {
    addSelectorWeight(i: [string, number]) {
      if (i[0] && i[1])
        (this.setting.selectorWeight as any)[i[0]] = i[1]
    }
  },
})
</script>

<style scoped>

</style>