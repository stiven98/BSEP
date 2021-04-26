<template>
    <div class="q-pa-xl q-mt-xl row justify-center">
      <div v-if="invalid" class="text-red text-h4"> Invalid or expired link </div>
      <div v-else class="text-green text-h4"> Account succesfully activated. <q-btn color="primary" label="Login" @click="$router.push('/')" /></div>
    </div>
</template>

<script>
export default {
  name: 'Activation',
  data: function () {
    return {
      id: this.$route.params.id,
      invalid: ''
    }
  },
  beforeMount () {
    this.$axios.post('http://localhost:8085/api/users/activate/' + this.id)
      .catch(err => {
        console.log(err)
        this.invalid = true
      })
  },
  methods: {
  }
}
</script>
