<template>
  <v-app dark>
    <v-app-bar
      fixed
      app
    >
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

export default {
  components: {Footer},
}
</script>
