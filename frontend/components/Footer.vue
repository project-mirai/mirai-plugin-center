<template>
  <v-footer
    app
    color="transparent"
    height="50"
    inset
  >
    <v-card class="transparent elevation-0" width="100%">
      <v-card-text>
        <span class="float-left">Mamoe Technologies &copy; {{ new Date().getFullYear() }} </span>
        <span class="float-right">
          <v-btn
            class="transparent elevation-0"
            href="https://github.com/mamoe"
            type="text"
            :ripple="false"
            plain
            small>Github</v-btn>

          <v-btn
            class="transparent elevation-0"
            href="https://github.com/mamoe"
            type="text"
            :ripple="false"
            plain
            small>源代码</v-btn>

        </span>

      </v-card-text>
    </v-card>
  </v-footer>

</template>

