<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <h1 class="font-weight-bold text-blue-grey-darken-4">
          Profile
        </h1>
      </v-col>
      <v-col cols="12">
        <v-card
            class="pa-4 bg-grey-lighten-3"
        >
          <v-card-title>
            Hello {{ userStore.getEmail }}
          </v-card-title>
          <v-card-content v-if="userStore.userHasRole(role.NEW)">
            Вам на почту было отправленно письмо с подтверждением. Обычно доставка письма занимает 1-2 минуты, но в
            исключительных случаях это может занять до 30 минут.<br>
            Не видите письма? Проверьте папку спам.<br>
            По прежнему не видите письма? Нажмите для повторной отправки.<br>
            <v-btn
                class="mt-4 indigo-darken-4"
                size="small"
                @click="resendEmailConfirmation"
            >
              Отправить
            </v-btn>
          </v-card-content>
          <v-card-content v-if="userStore.userHasRole(role.USER)">
            Ваша почта подтверждена
          </v-card-content>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import useVuelidate from "@vuelidate/core";
import {useAuthStore} from "@/store/AuthStore";
import {useUserStore} from "@/store/UserState";
import {UserRole} from "@/models/UserRole";

export default defineComponent({
  name: "ProfilePage",

  setup() {
    return {
      validate: useVuelidate(),
      authStore: useAuthStore(),
      userStore: useUserStore(),
      role: UserRole
    }
  },

  methods: {
    resendEmailConfirmation() {
      this.authStore.resendConfirmationEmail()
    }
  }
})
</script>

<style scoped>

</style>