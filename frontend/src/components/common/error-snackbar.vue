<template>
  <v-snackbar
      v-model="error"
      color="deep-orange-darken-1"
      centered
      top
  >
    {{ this.errorStore.getMessage }}
    <template v-slot:actions>
      <v-btn
          variant="text"
          @click="closeError"
      >
        Закрыть
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useErrorStore} from "@/store/ErrorStore";

export default defineComponent({
  name: "error-snackbar",

  setup() {
    return {
      errorStore: useErrorStore()
    }
  },

  computed: {
    error() {
        const errorStore = useErrorStore()
        return errorStore.hasError
    }
  },

  methods: {
    closeError() {
      this.errorStore.remove()
    }
  }
})
</script>

<style scoped>

</style>