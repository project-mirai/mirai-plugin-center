<template>
  <v-app dark>
    <v-navigation-drawer
      v-model="drawer"
      app
    >
      <drawer></drawer>
    </v-navigation-drawer>

    <v-app-bar
      fixed
      app
    >
      <v-app-bar-nav-icon @click="drawer=!drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Mirai插件中心</v-toolbar-title>
    </v-app-bar>

    <v-main>
      <v-container fill-height>
        <nuxt/>
      </v-container>
    </v-main>

    <Footer/>
  </v-app>

</template>

<script>

import Footer from "../components/Footer";
import Drawer from "../components/Drawer/Drawer";

export default {
  components: {Drawer, Footer},
  data() {
    return {
      drawer: null
    }
  }
}
</script>
