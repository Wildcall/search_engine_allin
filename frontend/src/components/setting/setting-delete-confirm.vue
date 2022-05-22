<template>
  <v-card
      class="bg-grey-lighten-3"
      style="width: 500px"
      elevation="5"
  >

    <v-card-title class="justify-center bg-blue-grey-darken-4">
      <div class="ml-4 text-teal-lighten-5">
        Удалить настройку
      </div>
      <v-spacer/>
      <div>
        <v-btn
            color="teal-lighten-5"
            icon="mdi-close"
            variant="text"
            @click="this.$emit('close')"
        />
      </div>
    </v-card-title>

    <div class="pa-4">
      <v-card-content>
        Вы действительно хотите удалить настройку {{ setting.name }}?<br/>
        При удалении так же будут удаленны все задачи связанные с этой настройкой, продолжить?
      </v-card-content>

      <v-divider class="mb-4 mt-4"/>

      <v-card-actions class="d-flex justify-center">
        <v-btn
            @click="this.$emit('delete')"
            color="error"
        >
          Удалить
        </v-btn>
      </v-card-actions>
    </div>
  </v-card>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "setting-delete-confirm",

  props: {
    setting: {}
  },

  methods: {}
})
</script>

<style scoped>

</style>