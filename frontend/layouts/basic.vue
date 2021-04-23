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
      <v-spacer></v-spacer>
      <v-btn icon @click="changeTheme">
        <v-icon>mdi-brightness-4</v-icon>
      </v-btn>
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
      drawer: null,
      dark: false
    }
  },
  methods:{
    changeTheme () {
      this.dark = !this.dark
      this.$vuetify.theme.dark = this.dark
    },
  }
}
</script>
