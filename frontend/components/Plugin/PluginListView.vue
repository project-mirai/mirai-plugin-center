<template>
  <v-row>
    <v-col cols="12" md="6" sm="12" v-for="(item,index) in info" :key="index">
      <plugin-info-card :info="item"></plugin-info-card>
    </v-col>
  </v-row>
</template>

<script>
import {generateFakePluginList} from "../../utils/plugin";
import PluginInfoCard from "./PluginInfoCard";

export default {
  name: "PluginListView",
  components: {PluginInfoCard},
  props: {
    info: {
      type: Array,
      default: function () {
        return generateFakePluginList()
      }
    }
  }
}
</script>

<style scoped>

</style>
