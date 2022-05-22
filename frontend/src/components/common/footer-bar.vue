<template>
  <v-app-bar
      position="bottom"
      elevation="0"
      class="bg-blue-grey-darken-4"
  >
    <v-row
    justify="center">
      <v-btn
          href="https://github.com/Wildcall/search_engine"
          target="_blank"
          class="text-teal-lighten-5"
      >
        Github
      </v-btn>
    </v-row>
  </v-app-bar>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "footer-bar"
})
</script>

<style scoped>

</style>