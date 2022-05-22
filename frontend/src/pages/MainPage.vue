<template>
  <v-container class="d-flex justify-center align-center">
    <v-card
        style="max-width: 50%; min-width: 500px"
        class="bg-grey-lighten-3"
        elevation="5"
    >
      <v-card-title class="justify-center">
        Search engine
      </v-card-title>
      <v-card-content>
        Проект создан исключительно в образовательных целях.<br/>
        Что умеет сервис:<br/>
        1. Совершать обход заданного сайта в глубину и сохранять содержимое страниц в базу данных<br/>
        2. Выделять слова и создавать индексы релевантности отдельных слов<br/>
        3. Совершать поиск по сайту на основании индексов<br/>
        Что не умеет сервис:<br/>
        1. Все что не описано выше<br/><br/>
        Ссылка на исходный код:<br/>
        https://github.com/Wildcall/search_engine
      </v-card-content>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "MainPage",
})
</script>

<style scoped>

</style>