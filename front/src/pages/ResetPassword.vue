<template>
    <div class="q-pa-xl q-mt-xl row justify-center">
      <div v-if="invalid" class="text-red text-h4"> Invalid or expired link </div>
      <div class="col-2" v-else >
        <q-form
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input
        filled
        v-model="pass"
        label="Enter password"
        type="password"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />

      <q-input
        filled
        type="password"
        v-model="pass2"
        label="Confirm password"
        lazy-rules
       :rules="[ val => val && val.length > 0 && val== pass || 'Please make sure your passwords match ']"
      />
      <div>
        <q-btn label="Reset" type="submit" color="primary"/>
      </div>
    </q-form>
      </div>
    </div>
</template>

<script>
export default {
  name: 'PageIndex',
  data: function () {
    return {
      pass: '',
      pass2: '',
      id: this.$route.params.id,
      invalid: ''
    }
  },
  beforeMount () {
    this.$axios.get('http://localhost:8085/api/users/checkRequest/' + this.id)
      .catch(err => {
        console.log(err)
        this.invalid = true
      })
  },
  methods: {
    onSubmit () {
      this.$axios.post('http://localhost:8085/api/users/resetPassword', {
        password: this.pass,
        password2: this.pass2,
        requestId: this.id
      })
        .then(response => {
          this.$q.notify({
            type: 'positive',
            message: 'Password succesfully changed.'
          })
          this.$router.push('/')
        })
    }
  }
}
</script>
