<template>

    <div style="max-width:400px">
        <q-form
      @submit="onSubmit"
      class="q-gutter-md"
    >

      <q-select filled v-model="issuer" :options="issuers" label="Issuer"
       lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"/>
       <q-select filled v-model="issuer" :options="issuers" label="Subject"
       lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"/>
       <q-input filled v-model="org" label="organization"/>

      <div>
        <q-btn label="Create certificate" type="submit" color="primary"/>
      </div>
    </q-form>

  </div>
</template>

<script>
export default {
  name: 'PageIndex',
  data: function () {
    return {
      issuers: [],
      subjects: [],
      issuer: null,
      subject: null,
      org: ''
    }
  },
  methods: {
    onSubmit () {
      if (this.username === 'admin' && this.pass === 'admin') {
        this.$router.push('/adminHome')
      }
    },
    getIssuers () {
      // to be implemented
    },
    getSubjects () {
      // to be implemented
    }
  }
}
</script>
