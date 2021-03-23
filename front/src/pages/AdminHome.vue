<template>
    <q-page padding>
      <q-dialog v-model="detailsDialog">
         <div class="q-gutter-y-md" style="max-width: 600px">
      <q-card>
        <q-tabs
          v-model="tab"
          dense
          class="bg-grey-3 text-grey-7"
          active-color="primary"
          indicator-color="purple"
          align="justify"
        >
          <q-tab name="details" label="Details" />
          <q-tab name="path" label="Certification path" />
        </q-tabs>

        <q-tab-panels v-model="tab" animated class="bg-primary text-white">
          <q-tab-panel name="details" v-if="selectedCard">
            Subject: {{selectedCard.subject}}
            <div>
            Common name: {{selectedCard.commonName}}
            </div>
            <div>
            First name: {{selectedCard.commonName}}
            </div>
            <div>
            Last name: {{selectedCard.commonName}}
            </div>
            <div>
            Valid from: {{formatDate(selectedCard.startDate)}}
            </div>
            <div>
            Valid to: {{formatDate(selectedCard.endDate)}}
            </div>
            <div>
            Organization: {{selectedCard.organization}}
            </div>
             <div>
            Organization unit: {{selectedCard.organizationUnit}}
            </div>
            <div>
            Country: {{selectedCard.country}}
            </div>
             <div>
            Email: {{selectedCard.email}}
            </div>
            <div>
            Type: {{selectedCard.certificateType}}
            </div>
             <div>
            Issuer type: {{selectedCard.issuerType}}
            </div>

          </q-tab-panel>

          <q-tab-panel name="path">
            <div class="text-h6">Path</div>
            Lorem ipsum dolor sit amet consectetur adipisicing elit.
          </q-tab-panel>
        </q-tab-panels>
      </q-card>
         </div>
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
        <q-btn flat>Revoke</q-btn>
      </q-card-actions>
    </q-card>
     </div>
    </q-page>
</template>
<script>
export default {
  name: 'PageIndex',
  data: function () {
    return {
      allCertificates: [],
      detailsDialog: false,
      selectedCard: null,
      tab: 'details'
    }
  },
  mounted () {
    this.$axios
      .get('http://localhost:8085/api/certificate/all')
      .then((response) => {
        this.allCertificates = response.data
      })
  },
  methods: {
    formatDate (date) {
      return date.split('T')[0]
    }
  }
}
</script>
