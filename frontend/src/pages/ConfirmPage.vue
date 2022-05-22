<template>
  <v-container>
    <v-card>
      <v-card-title>
        Подтверждение почты
      </v-card-title>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {useAuthStore} from "@/store/AuthStore";

export default defineComponent({
  name: "ConfirmPage",

  setup() {
    return {
      authStore: useAuthStore()
    }
  },

  mounted() {
    const token: string = this.$route.params.token as string
    this.authStore.confirmEmail(token)
  },
})
</script>

<style scoped>

</style>