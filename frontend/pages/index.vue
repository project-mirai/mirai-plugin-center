<template>
  <v-row justify="center" align="center">
    <v-col cols="12" sm="10" md="9">
      <plugin-list-view></plugin-list-view>
    </v-col>
    <v-pagination
      v-model="pageInfo.nowPage"
      :length="pageInfo.total"
      total-visible="7"
    ></v-pagination>
  </v-row>


</template>

<script>

import PluginListView from "../components/Plugin/PluginListView";
export default {
  components: {PluginListView},
  layout:"basic",
  data() {
    return {
      pageInfo: {
        total: 15,
        nowPage:1
      }
    }
  }
}
</script>
