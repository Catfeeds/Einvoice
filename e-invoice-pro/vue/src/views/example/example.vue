<style>

</style>

<template>
  <div>
    工程模板
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  mounted() {
    // 网络请求样例
    this.$http({
      url: "/portalMsgService?method=omk.kafka.logs.search&ent_id=0",
      method: "post",
      data: {}
    }).then(res => {
      console.log("returnData:", res);
    });
  },
  methods: {},
  created() {}
};
</script>


