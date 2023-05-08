
/**
 *   @ResponseBody// спец.аннотация. возвращаем данные, а не представления
 *   помечаем наши методы
 *
 *   @Controller
 * @RequestMapping("/api")
 * public class FirstRESTController {
 *
 *     @ResponseBody// спец.аннотация. возвращаем данные, а не представления
 *     @GetMapping("/sayHello")
 *     public String sayHello(){
 *         return "Hello World!";
 *     }
 *
 * }
 * Если бы не пометили @ResponseBody
 * то спринг бы искал шаблон в папке templates где обычно лежали наши представления.
 * а тут мы пометили,что это не представление .а данные сразу. вернет строку просто по запросу /api/sayHello
 *............................................................................................................
 * МОЖЕМ ПОМЕТИТЬ ЦЕЛЫЙ КЛАСС АННОТАЦИЕЙ @RestController и нам не нужно будет писать потом везде @ResponseBody
 * по умолчанию она будет везде использоваться на всех методах в этом классе
 *
 * @RestController это аннотация @Controller + @ResponseBody над каждым методом
 *
 *
 * @RestController
 * @RequestMapping("/api")
 * public class FirstRESTController {
 *
 *     @GetMapping("/sayHello")
 *     public String sayHello(){
 *         return "Hello World!";
 *     }
 *
 * }
 * ..............................................................................................................
 * @GetMapping("/{id}")
 *     public Person getPerson(@PathVariable("id") int id){
 *     return peopleService.findOne(id);// так же будет автоматом сконвертирован в Json
 *
 *     }
 *     в { } указывается что человек введет вручную. для перехода по id наших списка персон
 *.................................................................................................................
 * ......//////////////////////////////////////////////////////////////////////////////////////////////////////////
 * @ExceptionHandler специальная аннотация которой мы помечаем. В себя принимает необходимое исключение и
 * возвращает необходимый Json
 * @ExceptionHandler
 *     private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
 * // наш персонеррорреспонс внутри с сообщением и датой.
 *     PersonErrorResponse response = new PersonErrorResponse("Person with this id wasn't found",
 *             System.currentTimeMillis());
 * // возвращаем нашу сущность с респонс сообщением и датой и хттп статусом
 *     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
 *
 *     }
 *
 *
 *   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *                              Принимаем данные на сервере.
 *                                 Аннотация @RequestBody
 *
 *               Post запрос с Json внутри
 *  КЛИЕНТ ------------------------------------------> REST API
 *
 *
 *  Аннотация @ResponseBody-отдает Java объекты клиенту в виде Json
 *
 * @RequestBody- принимает Json от клиента и конвертирует его в Java объекты
 *
 * @PostMapping()
 * public ResponseEntity<HttpStatus> create(@RequestBody Person person,BindingResult bindingResult){
 *
 * }
 *
 * @RequestBody Person person - Json,который отправляет клиент,должен соответствовать нашему классу Person
 *
 *
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 *                                  DTO(Data Transfer Object)
 *
 *
 *   Клиент----------->Json------------>DTO----------->Модель
 *                                 class PersonDTO    class Person
 *
 *
 *   Это обычный Java класс(даже может быть без аннотаций @Entity и @Component).Объект этого класса и есть DTO
 *   Используется на уровне контроллера(глубже обычно мы его не предаем)
 *   Может использоваться как для запросов так и ответов
 *   Для запросов принимается в методе контроллера и конвертируется в объект модели
 *   Для ответов конвертируется из модели и отправляется клиенту
 *
 *   ................................................................
 *                                     Зачем нужен DTO?
 *    Модель-бизнес логика
 *    DTO-объект для передачи данных
 *
 *   Модель может отличаться от того,что приходит от клиента при создании или того,что мы хотим отправлять клиенту
 *   (разный набор полей)
 *
 *   Модель и DTO могут полностью совпадать-такое бывает достаточно часто.
 *   Но DTO используют все равно даже в таких случаях,потому что это облегчает дальнейшее изменение.
 *   Это хороший стиль программирования-разделять объект для передачи данных и модель.
 *   Благодаря этому,в будущем мы сможем менять модель отдельно-при этом не будет изменяться то, что мы
 *   отдаем клиенту или принимает от клиента,и отдельно менять DTO- при этом не будет меняться модель
 *
 *   ПРИМЕРЫ
 *
 *   В модели есть поля, которые не назначаются данными от клиента(то же поле id. его клиенту не нужно отправлять)
 *
 *   В модели не нужны все поля,которые приходят от клиента.Если мы хотим проверять пароль и подтверждение пароля
 *   на сервере,то в DTO будет 2 этих поля,но в модели храниться только сам пароль.
 *
 *   Хотим отправлять клиенту какие-то дополнительные поля,например уникальный токен в ответе.К бизнес логике этот
 *   токен не относится,помещаем его в DTO
 *...............................................................
 *
 * Добавили колонки в БД. created_at upgrade_at created_who
 *
 * аннотация @Column не нужна в полях DTO класса. DTO не связан с БД. так же не помечен @Entity @Table
 * id тоже нет. оно не приходит от клиента
 * Используется на уровне контроллера(глубже обычно мы его не предаем)
 *
 * ///////////////////////////////////////////////////////////
 *
 *  @PostMapping    // делаем запрос тела и проверку на валидность нашего персон и бд// заменили Person на PersonDTO в вложении
 *     public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
 *     if (bindingResult.hasErrors()){
 *     StringBuilder errorMsg = new StringBuilder();
 *
 *     List<FieldError> errors = bindingResult.getFieldErrors();
 *     for (FieldError error: errors){// для каждого филдеррор в списке ошибок ерроры
 *         errorMsg.append(error.getField())// на каком поле была совершена ошибка
 *                 .append(" - ").append(error.getDefaultMessage())// помимо поля где,скажем еще какая ошибка была
 *                 .append(";");// что бы ошибки не склеились в одну разделяем их знаком ;
 *     }
 *
 *     throw new PersonNotCreatedException(errorMsg.toString());// выкидываем исключение с сообщением через туСтринг
 *
 *     }
 *     peopleService.save(convertToPerson(personDTO));// кладем чела уже PersonDTo
 *     return ResponseEntity.ok(HttpStatus.OK);// возвращаем что все хорошо. HTTP с пустым телом и статусом 200
 *     }
 *
 *
 *    /// создали метод для конвертации Person  v Person Controller/////
 *     private Person convertToPerson(PersonDTO personDTO) {// берем наше DTO
 *         ModelMapper modelMapper = new ModelMapper();//  в нем задаем исходный объект и целевой класс(тот класс в объект которого хотим преобразовать)
 *
 *        return modelMapper.map(personDTO, Person.class);// хотим смапить из персонДТО в объект класса Персон
 *
 *     }
 *
 *
 *
 *   /////  в PeopleService помещаем что бы не перегружать PeopleController/////
 *
 *     @Transactional
 *     public void save(Person person){// сохраняем людей в бд
 *         enrichPerson(person);// добавим в этого чела доп.данные которые назначаются на самом сервере
 *         peopleRepository.save(person);
 *
 *
 *     //метод для доп.данных с сервера
 *     private void enrichPerson(Person person) {
 *
 *     person.setCreatedAt(LocalDateTime.now());
 *     person.setUpdatedAt(LocalDateTime.now());
 *     person.setCreatedWho("ADMIN");
 *     }
 * ///////////////////
 *
 *
 * недостаток нашего кода в этих 3 полях. мы копируем их вручную. а если будет 100 полей?
 * person.setName(personDTO.getName());// копируем поля из нашей сущности персон в нашу новую DTO сущность
 *  *         person.setAge(personDTO.getAge());
 *  *         person.setEmail(personDTO.getEmail());
 *
 *  Решается зависимостью ModelMapper. Она служит что бы мапить DTO в модель и наоборот/добавим в POM.xml
 * <dependency>
 *     <groupId>org.modelmapper</groupId>
 *     <artifactId>modelmapper</artifactId>
 *     <version>2.4.5</version>
 * </dependency>
 *
 *
 *     Но мы можем еще улучшить код и делигировать спрингу
 *     @SpringBootApplication
 * public class FirstRestAppApplication {
 *
 * 	public static void main(String[] args) {
 * 		SpringApplication.run(FirstRestAppApplication.class, args);
 *        }
 *
 *    @Bean // создали бин для мапера в контексте спринга
 * 	public ModelMapper modelMapper(){
 * 		return new ModelMapper();
 *    }
 *
 * }
 *
 * далее добавим его в контроллер
 *      private final PeopleService peopleService;
 *      private final ModelMapper modelMapper;
 * @Autowired
 *     public PeopleController(PeopleService peopleService, ModelMapper modelMapper, ModelMapper modelMapper1) {
 *         this.peopleService = peopleService;
 *     this.modelMapper = modelMapper1;
 * }
 *
 *
 *
 *    private Person convertToPerson(PersonDTO personDTO) {// берем наше DTO
 *
 *        return modelMapper.map(personDTO, Person.class);// хотим смапить из персонДТО в объект класса Персон
 *         // берет одинаковые поля из Person & PersonDTO и мапит их
 *
 *     }
 *     // еще метод зеркальный выше который. тут мапит модель в ДТО
 *     private PersonDTO converToPersonDTO(Person person){
 *     return modelMapper.map(person,PersonDTO.class);
 *
 *     }
 *
 *
 *
 *меняем контроллер
 *   @GetMapping()
 *     public List<PersonDTO> getPeople(){
 *     return peopleService.findAll().stream().map(this::converToPersonDTO)
 *     .collect(Collectors.toList());// Jackson конвертирует эти объекты в Json
 *     }
 *     @GetMapping("/{id}")
 *     public PersonDTO getPerson(@PathVariable("id") int id){
 *         // Статус 200. все ок
 *     return converToPersonDTO(peopleService.findOne(id));// так же будет автоматом сконвертирован в Json
 *
 *
 *
 *
 *
 *
 *
 */
