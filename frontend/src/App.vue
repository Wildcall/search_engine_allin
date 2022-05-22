<template>
  <v-app>
    <error-snackbar/>
    <navigation-bar/>
    <v-main
        class="bg-teal-lighten-4"
    >
      <router-view/>
    </v-main>
    <footer-bar/>
  </v-app>
</template>

<script lang="ts">
import NavigationBar from "@/components/common/navigation-bar.vue"
import FooterBar from "@/components/common/footer-bar.vue"
import {defineComponent} from 'vue'
import {mapStores} from "pinia";
import {useAuthStore} from "@/store/AuthStore";
import ErrorSnackbar from "@/components/common/error-snackbar.vue";

export default defineComponent({
  name: 'App',

  components: {
    ErrorSnackbar,
    NavigationBar,
    FooterBar
  },

  data() {
    return {}
  },

  computed: {
    ...mapStores(useAuthStore)
  },

  mounted() {
    this.authStore.getUserInfo()
  }
})
</script>

<style>

</style>
