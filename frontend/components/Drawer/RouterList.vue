<template>
  <v-list dense>
    <v-subheader>功能</v-subheader>
    <v-list-item-group
      v-model="selectedItem"
      color="primary"
    >
      <NuxtLink :to="item.link"  v-for="(item, index) in routers" :key="index" style="text-decoration:none;">
        <v-list-item>
          <v-list-item-icon>
            <v-icon v-text="item.icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title v-text="item.name"></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </NuxtLink>
    </v-list-item-group>
  </v-list>
</template>

<script>
import {getSelfRouter} from '../../utils/router'
export default {
  name: "RouterList",
  data() {
    return {
      logon: false,
      routers: [],
      selectedItem: null
    }
  },
  created() {
      this.routers = getSelfRouter()
      console.log(this.routers)
  }
}
</script>

<style scoped>

</style>
