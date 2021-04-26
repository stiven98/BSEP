<template>
    <q-page padding>
      <q-dialog v-model="detailsDialog" style="width:400px;height:1000px">
         <details-dialog :selectedCard="selectedCard"/>
      </q-dialog>
     <div class="q-pa-md row items-start q-gutter-md">
    <q-card v-for="c in allCertificates" v-bind:key="c.id" class="my-card">
      <q-card-section class="bg-primary text-white">
        <div class="text-h6">{{c.subject}}</div>
        <div class="text-subtitle2">Serial number: {{c.serialNumber}}</div>
        <div class="text-subtitle2">Valid from: {{formatDate(c.startDate)}} to: {{formatDate(c.endDate)}}  </div>
        <div class="text-subtitle2">Type: {{c.certificateType}}</div>
      </q-card-section>
      <q-separator />
      <q-card-actions align="right">
        <q-btn @click="selectedCard=c;detailsDialog=true">Details</q-btn>
        <q-btn @click="revokeCertificate(c.serialNumber)" flat>Revoke</q-btn>
        <q-btn @click="downloadCertificate(c.serialNumber)" flat>Download</q-btn>
      </q-card-actions>
    </q-card>
     </div>
    </q-page>
</template>
<script>
import DetailsDialog from 'src/components/DetailsDialog.vue'
export default {
  components: { DetailsDialog },
  name: 'PageIndex',
  data: function () {
    return {
      allCertificates: [],
      detailsDialog: false,
      selectedCard: null
    }
  },
  mounted () {
    this.$axios
      .get('https://localhost:8085/api/certificate/all')
      .then((response) => {
        this.allCertificates = response.data
      })
  },
  methods: {
    formatDate (date) {
      const dateTmp = date.toString()
      return dateTmp.split('T')[0]
    },
    revokeCertificate (serialNumber) {
      this.$axios
        .post('https://localhost:8085/api/certificate/revoke', serialNumber, { headers: { 'Content-Type': 'text/plain' } })
        .then(response => {
          this.$q.notify({
            type: 'positive',
            message: 'Certificate sucessfully revoked.'
          })
        })
        .catch(err => {
          console.log(err)
          this.$q.notify({
            type: 'negative',
            message: 'Error, revoking failed.'
          })
        })
    },
    downloadCertificate (serialNumber) {
      var fileLink = document.createElement('a')
      fileLink.href = 'https://localhost:8085/api/certificate/download?serialNumber=' + serialNumber
      document.body.appendChild(fileLink)
      fileLink.click()
    }
  }
}
</script>
