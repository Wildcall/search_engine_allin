<template>
  <v-card
      class="bg-grey-lighten-3"
      style="width: 500px"
      elevation="5"
  >
    <v-card-title class="justify-center bg-blue-grey-darken-4">
      <div class="ml-4 text-teal-lighten-5">
        Поиск
      </div>
      <v-spacer/>
      <div>
        <v-btn
            color="teal-lighten-5"
            icon="mdi-close"
            variant="text"
            @click="this.close"
        />
      </div>
    </v-card-title>
    <div class="pa-4">
      <div class="d-flex justify-space-between align-stretch">
        <v-text-field
            v-model="query"
            type="text"
            variant="outlined"
            density="compact"
            label="Поисковый запрос"
        />
        <div class="ml-4">
          <v-btn @click="search">
            Поиск
          </v-btn>
        </div>
      </div>
      <div v-for="(result, i) in searchResult"
           :key="i"
      >
        <div class="d-flex flex-column mb-4">
          <a target="_blank" :href="result.path">{{ result.title }}</a>
          <span class="text-caption">{{result.path}}</span>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {SearchService} from "@/services/SearchService";
import {SearchResponse} from "@/models/response/SearchResponse";

export default defineComponent({
  name: "search-dialog",

  data() {
    return {
      query: '',
      searchResult: [] as SearchResponse[]
    }
  },

  props: {
    siteId: Number
  },

  methods: {
    search() {
      SearchService.search(this.siteId as number, this.query as string)
          .then(r => {
            if (r)
              this.searchResult = r.data
          })
    },

    close() {
      this.reset()
      this.$emit('close')
    },

    reset() {
      this.query = ''
    }
  }
})
</script>

<style scoped>

</style>