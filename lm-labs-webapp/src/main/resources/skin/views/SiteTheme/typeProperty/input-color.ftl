<input type="text" class="small" name="quizBackgroundColorEdit" value="${property.value}" onchange="jQuery('#valueProperty${cptProperties}').val(this.value); " />
<input type="hidden" id="valueProperty${cptProperties}" name="valueProperty${cptProperties}" value="${property.value}" />
<script type="text/javascript">
  $("input[name=quizBackgroundColorEdit]").miniColors({
    change: function(hex, rgb) {
      jQuery('#valueProperty${cptProperties}').val(hex);
    }
  });
</script>