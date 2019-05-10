<template>
  <div id="main" class="app-main">
    <keep-alive exclude="invoice-list">
    <router-view></router-view>
    </keep-alive>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  mounted() {},
  beforeDestroy() {},
  methods: {}
};
</script>

<style>
html,
body {
  width: 100%;
  height: 100%;
}
.app-main {
  width: 100%;
  height: 100%;
}
</style>
