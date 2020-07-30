<template>
  <v-navigation-drawer
    v-model="$store.state.drawer"
    :clipped="$vuetify.breakpoint.lgAndUp"
    app
  >
    <v-list dense>
      <template v-for="item in items">
        <v-row
          v-if="item.heading"
          :key="item.heading"
          align="center"
        >
          <v-col cols="6">
            <v-subheader v-if="item.heading">
              {{ item.heading }}
            </v-subheader>
          </v-col>
          <v-col
            cols="6"
            class="text-center"
          >
            <a
              href="#!"
              class="body-2 black--text"
            >EDIT</a>
          </v-col>
        </v-row>
        <v-list-group
          v-else-if="item.children"
          :key="item.text"
          v-model="item.model"
          :prepend-icon="item.model ? item.icon : item['icon-alt']"
          append-icon=""
        >
          <template v-slot:activator>
            <v-list-item-content>
              <v-list-item-title>
                {{ item.text }}
              </v-list-item-title>
            </v-list-item-content>
          </template>
          <v-list-item
            v-for="(child, i) in item.children"
            :key="i"
            link
          >
            <v-list-item-action v-if="child.icon">
              <v-icon>{{ child.icon }}</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>
                {{ child.text }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-group>
        <v-list-item
          v-else
          :key="item.text"
          link
          @click="clickclick"
          :to="{ name: item.page }"
        >
          <v-list-item-action
          >
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              {{ item.text }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </template>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
export default {
    name: 'Sidebar',
    data: () => ({
      items: [
        { icon: 'mdi-home', text: 'HOME', page: 'Home' },
      { icon: 'mdi-contacts', text: 'My STudy', page: 'StudyDetail' },
      { icon: 'mdi-plus', text: 'Study 만들기', page: 'StudyCreate' },
      { icon: 'mdi-message', text: 'Contact Us', page: 'Contact' },
      { icon: 'mdi-help-circle', text: '가이드', page: 'Guide' },
      { icon: 'mdi-keyboard', text: '사이드바 닫기(미구현)', page: 'Main' },
      {
        icon: 'mdi-chevron-up',
        'icon-alt': 'mdi-chevron-down',
        text: '라벨(나중에 필요하면 쓸 것)',
        model: true,
        children: [
          { icon: 'mdi-plus', text: 'Create label' },
        ],
      },
      {
        icon: 'mdi-chevron-up',
        'icon-alt': 'mdi-chevron-down',
        text: '추가(나중에 필요하면 쓸 것)',
        model: false,
        children: [
          { text: 'Import' },
          { text: 'Export' },
          { text: 'Print' },
          { text: 'Undo changes' },
          { text: 'Other contacts' },
        ],
      },
    ],
    }),
    methods: {
      clickclick(event) {
        console.log('클릭미')
      }
    }
}
</script>

<style>

</style>