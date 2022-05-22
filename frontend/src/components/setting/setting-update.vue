<template>
  <v-card
      class="pa-4 bg-grey-lighten-3"
      style="width: 500px"
      elevation="5"
  >
    <v-card-title>
      <v-row
          justify="space-between"
          no-gutters
          class="mb-4"
      >
        <v-col lg="1"/>
        <v-col md="auto" class="d-flex align-center">
          Подтверждение удаления
        </v-col>
        <v-col lg="1">
          <v-btn
              icon="mdi-close"
              variant="text"
              @click="this.$emit('close')"
          />
        </v-col>
      </v-row>

    </v-card-title>
    <v-card-content>
      Вы действительно хотите удалить настройку {{ setting.name }}?<br/>
      При удалении так же будут удаленны все задачи связанные с этой настройкой, продолжить?
    </v-card-content>
    <v-card-actions class="d-flex justify-center">
      <v-btn
          @click="this.$emit('delete')"
          color="error"
      >
        Удалить
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import {defineComponent} from "vue";

export default defineComponent({
  name: "setting-update",

  props: {
    setting: {}
  },

  methods: {}
})
</script>

<style scoped>

</style>