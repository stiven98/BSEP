<template>
    <div class="q-pa-xl q-mt-xl row justify-center">
      <div class="col-2" >
        <q-form
      @submit="onSubmit"
      class="q-gutter-md"
    >
      <q-input
        filled
        v-model="pass"
        label="Enter password"
        lazy-rules
        :rules="[ val => val && val.length > 0 || 'Please type something']"
      />

      <q-input
        filled
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
      pass2: ''
    }
  },
  methods: {
    onSubmit () {
    }
  }
}
</script>
