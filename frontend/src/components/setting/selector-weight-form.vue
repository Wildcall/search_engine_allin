<template>
  <v-row>
    <v-col cols="2" class="d-flex justify-center">
      <v-btn
          :disabled="val.$invalid"
          size="small"
          variant="plain"
          text
          icon="mdi-plus"
          @click="addSelectorWeight"
      />
    </v-col>
    <v-col cols="5" class="d-flex justify-center align-center">
      <v-text-field
          v-model="selector"
          type="text"
          density="compact"
          variant="outlined"
          :error-messages="(val.selector.$dirty && val.selector.$invalid && selector) ? val.selector.$silentErrors[0].$message : ''"
      />
    </v-col>
    <v-col cols="5" class="d-flex justify-center align-center">
      <v-text-field
          v-model="weight"
          type="text"
          density="compact"
          variant="outlined"
          :error-messages="(val.weight.$dirty && val.weight.$invalid && weight) ? val.weight.$silentErrors[0].$message : ''"
      />
    </v-col>
  </v-row>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import useVuelidate from "@vuelidate/core";
import {maxLength, required, minValue, maxValue, numeric} from "@vuelidate/validators";

export default defineComponent({
  name: "selector-weight-form",

  setup() {
    return {
      val: useVuelidate()
    }
  },

  data() {
    return {
      selector: '',
      weight: 0
    }
  },

  validations() {
    return {
      selector: {
        $autoDirty: true,
        required: required,
        maxLength: maxLength(32)
      },
      weight: {
        $autoDirty: true,
        required: required,
        numeric: numeric,
        minValue: minValue(0.01),
        maxValue: maxValue(1)
      }
    }
  },

  methods: {
    async addSelectorWeight() {
      if (!await this.val.$validate()) return
      this.$emit('add', [this.selector, this.weight])
      this.selector = ''
      this.weight = 0
    }
  },
})
</script>

<style scoped>

</style>