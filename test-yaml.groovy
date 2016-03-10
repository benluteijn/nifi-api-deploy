import org.yaml.snakeyaml.Yaml

@Grab(group='org.yaml', module='snakeyaml', version='1.17')

def config = new Yaml().load(new File('nifi-deploy.yml').text)
assert config

assert config.nifi.url == 'http://192.168.99.103:9091'
assert config.nifi.templateUri == 'https://cwiki.apache.org/confluence/download/attachments/57904847/Hello_NiFi_Web_Service.xml?version=1&modificationDate=1449369797000&api=v2'
assert 'ENABLED' == config.controllerServices.StandardHttpContextMap.state

assert config.processGroups.size() == 2

def pg = config.processGroups['Hello NiFi Web Service']
assert pg.processors.entrySet().size() == 1

def p =  pg.processors.entrySet()[0]
assert p.key == 'Receive request and data'
assert p.value.state == 'RUNNING'

def c = p.value.config
assert c

assert c.'Listening Port' == 8000

def s = 'Location: http://192.168.99.103:9091/nifi-api/controller/templates/1c6bd4ca-c934-36fd-98cd-397d0dc0f27d'
println s[++s.lastIndexOf('/')..-1] // grabs the trailing UUID only
