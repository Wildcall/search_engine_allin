<template>
  <v-app-bar
      class="bg-blue-grey-darken-4"
      elevation="5"
  >
    <v-btn
        size="x-large"
        @click="$router.push('/')"
        class="text-teal-lighten-5"
    >
      SEARCH ENGINE
    </v-btn>
    <v-spacer/>
    <div v-if="this.authStore.getAuth">
      <v-btn
          @click="this.$router.push('/setting')"
          class="text-teal-lighten-5"
      >
        Настройки
      </v-btn>
      <v-btn
          @click="this.$router.push('/site')"
          class="text-teal-lighten-5"
      >
        Сайты
      </v-btn>
      <v-btn
          @click="this.$router.push('/profile')"
          class="text-teal-lighten-5"
      >
        Профиль
      </v-btn>
      <v-btn
          @click="logout"
          class="text-teal-lighten-5"
      >
        Выйти
      </v-btn>
    </div>
    <div v-else>
      <v-btn
          @click="openAuthForm"
          class="text-teal-lighten-5"
      >
        Войти
      </v-btn>
    </div>
    <v-dialog
        v-model="authForm"
        persistent
    >
      <auth-form
          @close="authForm = false"
      />
    </v-dialog>
  </v-app-bar>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useAuthStore} from "@/store/AuthStore";
import AuthForm from "@/components/common/auth-form.vue";

export default defineComponent({
  name: "navigation-bar",
  components: {AuthForm},

  setup() {
    return {
      authStore: useAuthStore(),
    }
  },

  data() {
    return {
      authForm: false
    }
  },

  methods: {
    openAuthForm() {
      this.authForm = true
    },

    logout() {
      this.authStore.logout()
      this.$router.push("/")
    }
  },
})
</script>

<style scoped>

</style>