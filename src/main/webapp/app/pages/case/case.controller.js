(function(){

    angular
        .module('BlurAdmin.pages.case.addcase',[])
        .controller('CaseController',CaseController);

    function CaseController($scope,$http,baConfig){

        $scope.getSlaves = getSlaves;
        $scope.excuteCase = excuteCase;
        $scope.addStep = addStep;
        $scope.selectedBrowser = {};
        $scope.selectedSlave = {};

        function getSlaves(){
            $http.get("/xamng/case/getSlaves").then(
                function (response) {
                    $scope.slaves = response.data.results;
                    return $scope.slaves;
                }
            );
        }

        function excuteCase(){
            $http.post(
                "/xamng/case/excute",
                {
                    slave:$scope.selectedSlave.value.id,
                    browser:$scope.selectedBrowser.value
                },
                {
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                    transformRequest: function(data){return $.param(data);}
                }
            ).success(function(data, status, headers, config){

            });
        }

        function addStep(){
            $scope.steps.push($($('#addStepPanel').find('input')[0]).val());
        };

        toDoListController($scope,baConfig);

      }

      function toDoListController(toListScope,vbaconfig){
        toListScope.transparent = vbaconfig.theme.blur;
        var dashboardColors = vbaconfig.colors.dashboard;
        var colors = [];
        for (var key in dashboardColors) {
          colors.push(dashboardColors[key]);
        }

        function getRandomColor() {
          var i = Math.floor(Math.random() * (colors.length - 1));
          return colors[i];
        }

        toListScope.todoList = [
          { text: 'Check me out' },
          { text: 'Lorem ipsum dolor sit amet, possit denique oportere at his, etiam corpora deseruisse te pro' },
          { text: 'Ex has semper alterum, expetenda dignissim' },
          { text: 'Vim an eius ocurreret abhorreant, id nam aeque persius ornatus.' },
          { text: 'Simul erroribus ad usu' },
          { text: 'Ei cum solet appareat, ex est graeci mediocritatem' },
          { text: 'Get in touch with akveo team' },
          { text: 'Write email to business cat' },
          { text: 'Have fun with blur admin' },
          { text: 'What do you think?' },
        ];

        toListScope.todoList.forEach(function(item) {
          item.color = getRandomColor();
        });

        toListScope.addToDoItem = function (event, clickPlus) {
          if (clickPlus || event.which === 13) {
            toListScope.todoList.unshift({
              text: toListScope.vm.newTodoText,
              color: getRandomColor(),
            });
            toListScope.vm.newTodoText = '';
          }
        };
      }
})();