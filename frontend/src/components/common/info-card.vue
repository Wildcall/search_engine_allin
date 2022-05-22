<template>
  <v-card
      :class="`pl-4 bg-${!selected ? style.line : style.bg}`"
      elevation="5"
  >
    <v-card
        :class="`pa-4 bg-${selected ? style.line : style.bg}`"
        flat
    >
      <v-card-title
          :class="'font-weight-bold text-' + style.text"
      >
        {{ type }}
      </v-card-title>
      <v-card-content>
        <v-row>
          <v-col>
            <h1>{{ data }}</h1>
          </v-col>
          <v-col
              v-if="button"
              class="d-flex align-center justify-end"
          >
            <v-btn
                :color="`${!selected ? style.line : style.bg}`"
                icon="mdi-plus"
                size="small"
                @click="this.$emit('add')"
            />
          </v-col>
        </v-row>
      </v-card-content>
    </v-card>
  </v-card>
</template>

<script lang="ts">

import {defineComponent} from "vue";
import {resourceStyle} from "@/style/ResourceStyle";

export default defineComponent({
      name: 'info-card',

      data() {
        return {
          style: {
            text: '',
            line: '',
            bg: ''
          }
        }
      },

      props: {
        type: String,
        selected: Boolean,
        button: Boolean,
        data: Number
      },

      mounted() {
        if (this.type) {
          this.style = resourceStyle(this.type)
        }
      }
    },
)
</script>
