package com.github.plokhotnyuk.jsoniter_scala.benchmark

import java.nio.charset.StandardCharsets.UTF_8
import com.github.plokhotnyuk.jsoniter_scala.benchmark.JsoniterScalaCodecs._
import com.github.plokhotnyuk.jsoniter_scala.core._
import org.openjdk.jmh.annotations.Setup

abstract class GeoJSONBenchmark extends CommonParams {
  var obj: GeoJSON.GeoJSON = _
  var jsonBytes: Array[Byte] = _
  var preallocatedBuf: Array[Byte] = _
  //Borders of Switzerland, from: https://github.com/mledoze/countries/blob/master/data/che.geo.json
  var jsonString1: String = """{"type":"FeatureCollection","features":[{"type":"Feature","properties":{"cca2":"ch"},"geometry":{"type":"Polygon","coordinates":[[[7.697223,47.543327],[7.769722,47.553329],[7.906388,47.558052],[7.91361,47.553886],[7.918888,47.552773],[7.931944,47.552216],[8.033333,47.556107],[8.086666,47.560555],[8.092222,47.561661],[8.099722,47.565552],[8.102499,47.568329],[8.103888,47.571938],[8.108055,47.578331],[8.110832,47.581108],[8.185276,47.617218],[8.193333,47.620827],[8.198889,47.621666],[8.205555,47.621666],[8.240833,47.619438],[8.283333,47.613052],[8.288055,47.611664],[8.2925,47.605827],[8.390833,47.579994],[8.435833,47.573608],[8.464998,47.602493],[8.477499,47.612221],[8.51111,47.631104],[8.519999,47.634163],[8.531387,47.632774],[8.558332,47.623329],[8.562222,47.621384],[8.565554,47.614441],[8.56943,47.598537],[8.57642,47.59137],[8.591389,47.599442],[8.594721,47.601662],[8.629166,47.646111],[8.630833,47.649719],[8.629444,47.653328],[8.626944,47.656105],[8.620832,47.66111],[8.610254,47.667732],[8.580555,47.66861],[8.551388,47.668327],[8.545832,47.667496],[8.536943,47.664444],[8.528055,47.656944],[8.525833,47.65361],[8.518332,47.649719],[8.495277,47.647217],[8.475555,47.646942],[8.424721,47.669441],[8.413332,47.678329],[8.407221,47.699715],[8.406666,47.703888],[8.473331,47.767494],[8.47611,47.769997],[8.479443,47.772499],[8.559721,47.806389],[8.566111,47.806938],[8.60861,47.804161],[8.614721,47.803604],[8.661388,47.79805],[8.685555,47.786659],[8.734999,47.761383],[8.736944,47.758049],[8.736944,47.75444],[8.734999,47.713882],[8.734165,47.709717],[8.730698,47.705673],[8.729166,47.703888],[8.720222,47.696625],[8.745277,47.695],[8.761389,47.692215],[8.800133,47.681725],[8.774721,47.710274],[8.772778,47.713333],[8.772221,47.717499],[8.777777,47.722771],[8.799999,47.735275],[8.804165,47.737221],[8.808887,47.735832],[8.835278,47.718048],[8.855833,47.698883],[8.883299,47.654148],[8.892776,47.651382],[8.904999,47.650276],[8.917776,47.650551],[8.934166,47.653053],[8.947777,47.657494],[8.973331,47.667496],[8.985554,47.672775],[8.996666,47.679161],[9.004999,47.682495],[9.024166,47.687775],[9.0425,47.688889],[9.078333,47.685829],[9.086943,47.684998],[9.261095,47.662842],[9.478322,47.575851],[9.566724,47.540451],[9.557772,47.505783],[9.559721,47.499718],[9.562222,47.496666],[9.567499,47.491104],[9.590277,47.470276],[9.594166,47.46833],[9.611666,47.468887],[9.646944,47.460274],[9.65111,47.458328],[9.65361,47.455551],[9.673332,47.398605],[9.673054,47.394997],[9.669998,47.387772],[9.664999,47.381386],[9.650555,47.373055],[9.641666,47.369995],[9.631388,47.36805],[9.625832,47.367218],[9.624422,47.367252],[9.614443,47.367493],[9.605276,47.359993],[9.599722,47.354164],[9.539999,47.284164],[9.533569,47.274544],[9.502222,47.231384],[9.491388,47.215271],[9.485832,47.200272],[9.484165,47.192215],[9.484444,47.179718],[9.489721,47.16555],[9.49861,47.15361],[9.505554,47.139999],[9.511389,47.121666],[9.512777,47.109444],[9.512499,47.100273],[9.510277,47.092499],[9.474637,47.057457],[9.485277,47.061104],[9.506388,47.06472],[9.538055,47.07],[9.544722,47.07],[9.598635,47.063835],[9.6325,47.056664],[9.719444,47.050278],[9.870277,47.023598],[9.875555,47.022491],[9.891943,47.003876],[9.893055,47.000267],[9.889444,46.993324],[9.880833,46.98082],[9.875555,46.970543],[9.873333,46.962769],[9.87361,46.958603],[9.87611,46.941658],[9.879444,46.939156],[9.934999,46.912773],[10.058887,46.866386],[10.109444,46.850273],[10.122776,46.849716],[10.140276,46.851105],[10.141807,46.851341],[10.15111,46.852776],[10.170555,46.857216],[10.22361,46.874992],[10.231388,46.878883],[10.313332,46.933609],[10.323332,46.955551],[10.345833,46.986382],[10.353611,46.994995],[10.357222,46.997215],[10.385555,47.002777],[10.391109,47.003609],[10.400833,47.001106],[10.429998,46.984161],[10.487499,46.937775],[10.488913,46.934212],[10.471235,46.871353],[10.447498,46.763054],[10.470833,46.635826],[10.486387,46.619438],[10.486666,46.615273],[10.486111,46.594719],[10.485277,46.590553],[10.475555,46.562492],[10.471109,46.551666],[10.465277,46.546387],[10.457531,46.542503],[10.44972,46.539162],[10.337221,46.549438],[10.305555,46.553886],[10.301666,46.555832],[10.247499,46.583885],[10.245554,46.587219],[10.245277,46.59111],[10.246944,46.599442],[10.253054,46.613884],[10.254166,46.61805],[10.253611,46.622215],[10.241665,46.636665],[10.238609,46.638885],[10.233332,46.639999],[10.18111,46.632217],[10.119165,46.611382],[10.0525,46.543053],[10.050278,46.539993],[10.044722,46.507217],[10.044167,46.498055],[10.044443,46.453331],[10.044998,46.449165],[10.046665,46.445831],[10.049166,46.443054],[10.055555,46.438332],[10.067221,46.432495],[10.08861,46.424164],[10.092777,46.423332],[10.153889,46.390274],[10.177221,46.272499],[10.176388,46.268326],[10.172499,46.257217],[10.169722,46.25444],[10.140833,46.23333],[10.133888,46.228882],[10.129999,46.227219],[10.078609,46.220551],[10.072777,46.220276],[10.066666,46.221107],[10.063055,46.222771],[10.05722,46.22805],[9.994999,46.291664],[9.982498,46.331383],[9.981943,46.334717],[9.991388,46.346664],[9.99361,46.354439],[9.991943,46.357773],[9.984722,46.361938],[9.949999,46.379166],[9.943888,46.379997],[9.91111,46.383331],[9.904999,46.383049],[9.852777,46.36972],[9.813055,46.35833],[9.770555,46.342209],[9.714998,46.302773],[9.708332,46.298332],[9.696943,46.297218],[9.624722,46.291939],[9.581944,46.296944],[9.547222,46.30555],[9.5425,46.306938],[9.521387,46.319717],[9.458611,46.38472],[9.456665,46.387772],[9.450277,46.426666],[9.455,46.472496],[9.372776,46.5075],[9.360832,46.508606],[9.302221,46.504715],[9.296944,46.503883],[9.288887,46.500275],[9.281944,46.495827],[9.273611,46.487778],[9.249722,46.448051],[9.25,46.440277],[9.253054,46.433327],[9.256943,46.431389],[9.272778,46.428329],[9.280554,46.423332],[9.282221,46.419998],[9.296944,46.35305],[9.297499,46.348885],[9.293333,46.32666],[9.291943,46.323051],[9.276667,46.292496],[9.24341,46.23671],[9.238609,46.230553],[9.189444,46.186661],[9.180555,46.179161],[9.177221,46.176941],[9.158609,46.171661],[9.144439,46.168049],[9.132221,46.160271],[9.083332,46.121109],[9.035831,46.05722],[9.016666,46.022499],[8.998055,45.976944],[8.996666,45.973328],[9.0175,45.94416],[9.025,45.935829],[9.031944,45.931664],[9.066666,45.924164],[9.079443,45.915833],[9.081388,45.912498],[9.085554,45.901939],[9.085554,45.899162],[9.083887,45.895554],[9.036665,45.837776],[8.998333,45.829437],[8.951111,45.845276],[8.93611,45.868889],[8.936666,45.872772],[8.936388,45.876938],[8.926666,45.900833],[8.9,45.949715],[8.895555,45.955826],[8.826666,45.984161],[8.813055,45.988609],[8.816944,46.031662],[8.82361,46.03611],[8.849998,46.054443],[8.852499,46.056938],[8.853333,46.061104],[8.850555,46.072495],[8.846666,46.079163],[8.844166,46.08194],[8.835278,46.089439],[8.826111,46.096664],[8.818333,46.100555],[8.747221,46.121109],[8.728855,46.108589],[8.72361,46.105553],[8.714998,46.102493],[8.701111,46.10083],[8.691944,46.101387],[8.617777,46.120277],[8.613888,46.122215],[8.460554,46.23333],[8.445555,46.245827],[8.444443,46.248604],[8.434166,46.291107],[8.434999,46.295273],[8.443333,46.317215],[8.447498,46.323326],[8.458332,46.33416],[8.465832,46.36805],[8.46611,46.37722],[8.461666,46.444443],[8.459999,46.452217],[8.451666,46.459999],[8.447498,46.461937],[8.436388,46.463333],[8.430277,46.463051],[8.365276,46.453606],[8.357222,46.449997],[8.30611,46.424721],[8.302776,46.422493],[8.299999,46.419716],[8.139999,46.226105],[8.143888,46.219719],[8.158333,46.188889],[8.159443,46.180832],[8.158888,46.176666],[8.150276,46.153053],[8.148888,46.149437],[8.014721,46.013329],[8.00861,46.008331],[7.998055,46.00222],[7.973055,45.996941],[7.95,45.994164],[7.911111,45.994164],[7.902499,45.991104],[7.881944,45.977776],[7.876666,45.972496],[7.866388,45.948326],[7.855742,45.919052],[7.8483,45.918755],[7.783889,45.923607],[7.743333,45.93222],[7.709167,45.943329],[7.653055,45.979996],[7.647499,45.98111],[7.536666,45.981667],[7.458055,45.941109],[7.429722,45.929443],[7.393332,45.916107],[7.378888,45.914719],[7.338888,45.920555],[7.300278,45.923882],[7.295555,45.92305],[7.291111,45.921944],[7.230277,45.898048],[7.196944,45.882217],[7.188611,45.879166],[7.183055,45.879166],[7.117499,45.880829],[7.105,45.882217],[7.099444,45.883606],[7.09111,45.887497],[7.077777,45.896942],[7.062778,45.909164],[7.038054,45.931938],[7.030555,45.961937],[7.008611,45.996666],[7.005833,45.999443],[6.933055,46.055275],[6.873888,46.088051],[6.788055,46.14222],[6.783889,46.148048],[6.781111,46.162498],[6.780833,46.166107],[6.799999,46.378326],[6.807016,46.404228],[6.809444,46.415276],[6.808332,46.41861],[6.803055,46.427773],[6.800278,46.43055],[6.791389,46.434166],[6.737778,46.447495],[6.702777,46.45472],[6.633611,46.464165],[6.52,46.45916],[6.510278,46.457771],[6.491944,46.453049],[6.328333,46.406944],[6.31111,46.401382],[6.295555,46.394165],[6.246388,46.357773],[6.243333,46.354996],[6.236388,46.344719],[6.23,46.333885],[6.225555,46.322495],[6.228055,46.319717],[6.243747,46.31551],[6.271944,46.26194],[6.245555,46.21833],[6.189166,46.173332],[6.185833,46.17083],[6.152222,46.15361],[6.133611,46.149437],[6.12361,46.148048],[5.996388,46.146942],[5.967222,46.201942],[5.96611,46.205276],[5.966666,46.209442],[5.968055,46.212494],[5.981388,46.221939],[6.009166,46.233887],[6.03,46.241386],[6.060833,46.250549],[6.072499,46.249718],[6.0825,46.246666],[6.107778,46.253052],[6.117222,46.260551],[6.119166,46.264442],[6.118055,46.268051],[6.106109,46.297489],[6.115,46.30555],[6.120277,46.311943],[6.154166,46.363052],[6.15861,46.369995],[6.158333,46.373886],[6.157222,46.37722],[6.151667,46.386383],[6.148333,46.388611],[6.111111,46.409721],[6.071111,46.426384],[6.086944,46.44944],[6.135278,46.539719],[6.129722,46.583054],[6.127222,46.585831],[6.127777,46.589996],[6.129999,46.59333],[6.269166,46.682777],[6.346666,46.711388],[6.355,46.714439],[6.366666,46.720551],[6.434999,46.758049],[6.452777,46.774437],[6.4575,46.78083],[6.458611,46.785271],[6.446944,46.839439],[6.460278,46.895828],[6.528333,46.971664],[6.566388,46.979996],[6.620832,46.994438],[6.629722,46.997215],[6.639722,47.004166],[6.678333,47.034164],[6.699444,47.063889],[6.793333,47.130829],[6.849721,47.164989],[6.838888,47.16777],[6.8375,47.171104],[6.839722,47.173607],[6.971666,47.291939],[7.000833,47.364998],[6.980833,47.361664],[6.931389,47.358887],[6.88111,47.356941],[6.879999,47.360275],[6.88111,47.364716],[6.884444,47.372772],[6.990555,47.497215],[7.025,47.508049],[7.14642,47.499054],[7.178333,47.445831],[7.187499,47.442215],[7.241111,47.421944],[7.246944,47.42083],[7.251389,47.421944],[7.275555,47.432495],[7.308332,47.440277],[7.336944,47.439438],[7.343611,47.438606],[7.355897,47.434017],[7.358055,47.433609],[7.3825,47.432495],[7.388333,47.434166],[7.415555,47.445274],[7.433054,47.460548],[7.452777,47.469994],[7.496944,47.494995],[7.502222,47.500549],[7.497222,47.544441],[7.49861,47.54805],[7.501389,47.550552],[7.513055,47.556664],[7.521667,47.559715],[7.546665,47.565552],[7.550833,47.576385],[7.557221,47.581383],[7.567222,47.583611],[7.586944,47.584999],[7.588268,47.58448],[7.640555,47.603882],[7.674444,47.606384],[7.676944,47.603607],[7.671944,47.579437],[7.669999,47.576385],[7.666666,47.574165],[7.621111,47.561104],[7.656111,47.550552],[7.697223,47.543327]],[[8.710255,47.696808],[8.709721,47.70694],[8.708332,47.710548],[8.705,47.713051],[8.698889,47.713608],[8.675278,47.712494],[8.670555,47.711105],[8.670277,47.707497],[8.673298,47.701771],[8.675554,47.697495],[8.678595,47.693344],[8.710255,47.696808]]]}}]}"""
  var jsonString2: String = """{"features":[{"properties":{"cca2":"ch"},"geometry":{"coordinates":[[[7.697223,47.543327],[7.769722,47.553329],[7.906388,47.558052],[7.91361,47.553886],[7.918888,47.552773],[7.931944,47.552216],[8.033333,47.556107],[8.086666,47.560555],[8.092222,47.561661],[8.099722,47.565552],[8.102499,47.568329],[8.103888,47.571938],[8.108055,47.578331],[8.110832,47.581108],[8.185276,47.617218],[8.193333,47.620827],[8.198889,47.621666],[8.205555,47.621666],[8.240833,47.619438],[8.283333,47.613052],[8.288055,47.611664],[8.2925,47.605827],[8.390833,47.579994],[8.435833,47.573608],[8.464998,47.602493],[8.477499,47.612221],[8.51111,47.631104],[8.519999,47.634163],[8.531387,47.632774],[8.558332,47.623329],[8.562222,47.621384],[8.565554,47.614441],[8.56943,47.598537],[8.57642,47.59137],[8.591389,47.599442],[8.594721,47.601662],[8.629166,47.646111],[8.630833,47.649719],[8.629444,47.653328],[8.626944,47.656105],[8.620832,47.66111],[8.610254,47.667732],[8.580555,47.66861],[8.551388,47.668327],[8.545832,47.667496],[8.536943,47.664444],[8.528055,47.656944],[8.525833,47.65361],[8.518332,47.649719],[8.495277,47.647217],[8.475555,47.646942],[8.424721,47.669441],[8.413332,47.678329],[8.407221,47.699715],[8.406666,47.703888],[8.473331,47.767494],[8.47611,47.769997],[8.479443,47.772499],[8.559721,47.806389],[8.566111,47.806938],[8.60861,47.804161],[8.614721,47.803604],[8.661388,47.79805],[8.685555,47.786659],[8.734999,47.761383],[8.736944,47.758049],[8.736944,47.75444],[8.734999,47.713882],[8.734165,47.709717],[8.730698,47.705673],[8.729166,47.703888],[8.720222,47.696625],[8.745277,47.695],[8.761389,47.692215],[8.800133,47.681725],[8.774721,47.710274],[8.772778,47.713333],[8.772221,47.717499],[8.777777,47.722771],[8.799999,47.735275],[8.804165,47.737221],[8.808887,47.735832],[8.835278,47.718048],[8.855833,47.698883],[8.883299,47.654148],[8.892776,47.651382],[8.904999,47.650276],[8.917776,47.650551],[8.934166,47.653053],[8.947777,47.657494],[8.973331,47.667496],[8.985554,47.672775],[8.996666,47.679161],[9.004999,47.682495],[9.024166,47.687775],[9.0425,47.688889],[9.078333,47.685829],[9.086943,47.684998],[9.261095,47.662842],[9.478322,47.575851],[9.566724,47.540451],[9.557772,47.505783],[9.559721,47.499718],[9.562222,47.496666],[9.567499,47.491104],[9.590277,47.470276],[9.594166,47.46833],[9.611666,47.468887],[9.646944,47.460274],[9.65111,47.458328],[9.65361,47.455551],[9.673332,47.398605],[9.673054,47.394997],[9.669998,47.387772],[9.664999,47.381386],[9.650555,47.373055],[9.641666,47.369995],[9.631388,47.36805],[9.625832,47.367218],[9.624422,47.367252],[9.614443,47.367493],[9.605276,47.359993],[9.599722,47.354164],[9.539999,47.284164],[9.533569,47.274544],[9.502222,47.231384],[9.491388,47.215271],[9.485832,47.200272],[9.484165,47.192215],[9.484444,47.179718],[9.489721,47.16555],[9.49861,47.15361],[9.505554,47.139999],[9.511389,47.121666],[9.512777,47.109444],[9.512499,47.100273],[9.510277,47.092499],[9.474637,47.057457],[9.485277,47.061104],[9.506388,47.06472],[9.538055,47.07],[9.544722,47.07],[9.598635,47.063835],[9.6325,47.056664],[9.719444,47.050278],[9.870277,47.023598],[9.875555,47.022491],[9.891943,47.003876],[9.893055,47.000267],[9.889444,46.993324],[9.880833,46.98082],[9.875555,46.970543],[9.873333,46.962769],[9.87361,46.958603],[9.87611,46.941658],[9.879444,46.939156],[9.934999,46.912773],[10.058887,46.866386],[10.109444,46.850273],[10.122776,46.849716],[10.140276,46.851105],[10.141807,46.851341],[10.15111,46.852776],[10.170555,46.857216],[10.22361,46.874992],[10.231388,46.878883],[10.313332,46.933609],[10.323332,46.955551],[10.345833,46.986382],[10.353611,46.994995],[10.357222,46.997215],[10.385555,47.002777],[10.391109,47.003609],[10.400833,47.001106],[10.429998,46.984161],[10.487499,46.937775],[10.488913,46.934212],[10.471235,46.871353],[10.447498,46.763054],[10.470833,46.635826],[10.486387,46.619438],[10.486666,46.615273],[10.486111,46.594719],[10.485277,46.590553],[10.475555,46.562492],[10.471109,46.551666],[10.465277,46.546387],[10.457531,46.542503],[10.44972,46.539162],[10.337221,46.549438],[10.305555,46.553886],[10.301666,46.555832],[10.247499,46.583885],[10.245554,46.587219],[10.245277,46.59111],[10.246944,46.599442],[10.253054,46.613884],[10.254166,46.61805],[10.253611,46.622215],[10.241665,46.636665],[10.238609,46.638885],[10.233332,46.639999],[10.18111,46.632217],[10.119165,46.611382],[10.0525,46.543053],[10.050278,46.539993],[10.044722,46.507217],[10.044167,46.498055],[10.044443,46.453331],[10.044998,46.449165],[10.046665,46.445831],[10.049166,46.443054],[10.055555,46.438332],[10.067221,46.432495],[10.08861,46.424164],[10.092777,46.423332],[10.153889,46.390274],[10.177221,46.272499],[10.176388,46.268326],[10.172499,46.257217],[10.169722,46.25444],[10.140833,46.23333],[10.133888,46.228882],[10.129999,46.227219],[10.078609,46.220551],[10.072777,46.220276],[10.066666,46.221107],[10.063055,46.222771],[10.05722,46.22805],[9.994999,46.291664],[9.982498,46.331383],[9.981943,46.334717],[9.991388,46.346664],[9.99361,46.354439],[9.991943,46.357773],[9.984722,46.361938],[9.949999,46.379166],[9.943888,46.379997],[9.91111,46.383331],[9.904999,46.383049],[9.852777,46.36972],[9.813055,46.35833],[9.770555,46.342209],[9.714998,46.302773],[9.708332,46.298332],[9.696943,46.297218],[9.624722,46.291939],[9.581944,46.296944],[9.547222,46.30555],[9.5425,46.306938],[9.521387,46.319717],[9.458611,46.38472],[9.456665,46.387772],[9.450277,46.426666],[9.455,46.472496],[9.372776,46.5075],[9.360832,46.508606],[9.302221,46.504715],[9.296944,46.503883],[9.288887,46.500275],[9.281944,46.495827],[9.273611,46.487778],[9.249722,46.448051],[9.25,46.440277],[9.253054,46.433327],[9.256943,46.431389],[9.272778,46.428329],[9.280554,46.423332],[9.282221,46.419998],[9.296944,46.35305],[9.297499,46.348885],[9.293333,46.32666],[9.291943,46.323051],[9.276667,46.292496],[9.24341,46.23671],[9.238609,46.230553],[9.189444,46.186661],[9.180555,46.179161],[9.177221,46.176941],[9.158609,46.171661],[9.144439,46.168049],[9.132221,46.160271],[9.083332,46.121109],[9.035831,46.05722],[9.016666,46.022499],[8.998055,45.976944],[8.996666,45.973328],[9.0175,45.94416],[9.025,45.935829],[9.031944,45.931664],[9.066666,45.924164],[9.079443,45.915833],[9.081388,45.912498],[9.085554,45.901939],[9.085554,45.899162],[9.083887,45.895554],[9.036665,45.837776],[8.998333,45.829437],[8.951111,45.845276],[8.93611,45.868889],[8.936666,45.872772],[8.936388,45.876938],[8.926666,45.900833],[8.9,45.949715],[8.895555,45.955826],[8.826666,45.984161],[8.813055,45.988609],[8.816944,46.031662],[8.82361,46.03611],[8.849998,46.054443],[8.852499,46.056938],[8.853333,46.061104],[8.850555,46.072495],[8.846666,46.079163],[8.844166,46.08194],[8.835278,46.089439],[8.826111,46.096664],[8.818333,46.100555],[8.747221,46.121109],[8.728855,46.108589],[8.72361,46.105553],[8.714998,46.102493],[8.701111,46.10083],[8.691944,46.101387],[8.617777,46.120277],[8.613888,46.122215],[8.460554,46.23333],[8.445555,46.245827],[8.444443,46.248604],[8.434166,46.291107],[8.434999,46.295273],[8.443333,46.317215],[8.447498,46.323326],[8.458332,46.33416],[8.465832,46.36805],[8.46611,46.37722],[8.461666,46.444443],[8.459999,46.452217],[8.451666,46.459999],[8.447498,46.461937],[8.436388,46.463333],[8.430277,46.463051],[8.365276,46.453606],[8.357222,46.449997],[8.30611,46.424721],[8.302776,46.422493],[8.299999,46.419716],[8.139999,46.226105],[8.143888,46.219719],[8.158333,46.188889],[8.159443,46.180832],[8.158888,46.176666],[8.150276,46.153053],[8.148888,46.149437],[8.014721,46.013329],[8.00861,46.008331],[7.998055,46.00222],[7.973055,45.996941],[7.95,45.994164],[7.911111,45.994164],[7.902499,45.991104],[7.881944,45.977776],[7.876666,45.972496],[7.866388,45.948326],[7.855742,45.919052],[7.8483,45.918755],[7.783889,45.923607],[7.743333,45.93222],[7.709167,45.943329],[7.653055,45.979996],[7.647499,45.98111],[7.536666,45.981667],[7.458055,45.941109],[7.429722,45.929443],[7.393332,45.916107],[7.378888,45.914719],[7.338888,45.920555],[7.300278,45.923882],[7.295555,45.92305],[7.291111,45.921944],[7.230277,45.898048],[7.196944,45.882217],[7.188611,45.879166],[7.183055,45.879166],[7.117499,45.880829],[7.105,45.882217],[7.099444,45.883606],[7.09111,45.887497],[7.077777,45.896942],[7.062778,45.909164],[7.038054,45.931938],[7.030555,45.961937],[7.008611,45.996666],[7.005833,45.999443],[6.933055,46.055275],[6.873888,46.088051],[6.788055,46.14222],[6.783889,46.148048],[6.781111,46.162498],[6.780833,46.166107],[6.799999,46.378326],[6.807016,46.404228],[6.809444,46.415276],[6.808332,46.41861],[6.803055,46.427773],[6.800278,46.43055],[6.791389,46.434166],[6.737778,46.447495],[6.702777,46.45472],[6.633611,46.464165],[6.52,46.45916],[6.510278,46.457771],[6.491944,46.453049],[6.328333,46.406944],[6.31111,46.401382],[6.295555,46.394165],[6.246388,46.357773],[6.243333,46.354996],[6.236388,46.344719],[6.23,46.333885],[6.225555,46.322495],[6.228055,46.319717],[6.243747,46.31551],[6.271944,46.26194],[6.245555,46.21833],[6.189166,46.173332],[6.185833,46.17083],[6.152222,46.15361],[6.133611,46.149437],[6.12361,46.148048],[5.996388,46.146942],[5.967222,46.201942],[5.96611,46.205276],[5.966666,46.209442],[5.968055,46.212494],[5.981388,46.221939],[6.009166,46.233887],[6.03,46.241386],[6.060833,46.250549],[6.072499,46.249718],[6.0825,46.246666],[6.107778,46.253052],[6.117222,46.260551],[6.119166,46.264442],[6.118055,46.268051],[6.106109,46.297489],[6.115,46.30555],[6.120277,46.311943],[6.154166,46.363052],[6.15861,46.369995],[6.158333,46.373886],[6.157222,46.37722],[6.151667,46.386383],[6.148333,46.388611],[6.111111,46.409721],[6.071111,46.426384],[6.086944,46.44944],[6.135278,46.539719],[6.129722,46.583054],[6.127222,46.585831],[6.127777,46.589996],[6.129999,46.59333],[6.269166,46.682777],[6.346666,46.711388],[6.355,46.714439],[6.366666,46.720551],[6.434999,46.758049],[6.452777,46.774437],[6.4575,46.78083],[6.458611,46.785271],[6.446944,46.839439],[6.460278,46.895828],[6.528333,46.971664],[6.566388,46.979996],[6.620832,46.994438],[6.629722,46.997215],[6.639722,47.004166],[6.678333,47.034164],[6.699444,47.063889],[6.793333,47.130829],[6.849721,47.164989],[6.838888,47.16777],[6.8375,47.171104],[6.839722,47.173607],[6.971666,47.291939],[7.000833,47.364998],[6.980833,47.361664],[6.931389,47.358887],[6.88111,47.356941],[6.879999,47.360275],[6.88111,47.364716],[6.884444,47.372772],[6.990555,47.497215],[7.025,47.508049],[7.14642,47.499054],[7.178333,47.445831],[7.187499,47.442215],[7.241111,47.421944],[7.246944,47.42083],[7.251389,47.421944],[7.275555,47.432495],[7.308332,47.440277],[7.336944,47.439438],[7.343611,47.438606],[7.355897,47.434017],[7.358055,47.433609],[7.3825,47.432495],[7.388333,47.434166],[7.415555,47.445274],[7.433054,47.460548],[7.452777,47.469994],[7.496944,47.494995],[7.502222,47.500549],[7.497222,47.544441],[7.49861,47.54805],[7.501389,47.550552],[7.513055,47.556664],[7.521667,47.559715],[7.546665,47.565552],[7.550833,47.576385],[7.557221,47.581383],[7.567222,47.583611],[7.586944,47.584999],[7.588268,47.58448],[7.640555,47.603882],[7.674444,47.606384],[7.676944,47.603607],[7.671944,47.579437],[7.669999,47.576385],[7.666666,47.574165],[7.621111,47.561104],[7.656111,47.550552],[7.697223,47.543327]],[[8.710255,47.696808],[8.709721,47.70694],[8.708332,47.710548],[8.705,47.713051],[8.698889,47.713608],[8.675278,47.712494],[8.670555,47.711105],[8.670277,47.707497],[8.673298,47.701771],[8.675554,47.697495],[8.678595,47.693344],[8.710255,47.696808]]],"type":"Polygon"},"type":"Feature"}],"type":"FeatureCollection"}"""
  var jsonString3: String = """{"features":[{"geometry":{"coordinates":[[[7.697223,47.543327],[7.769722,47.553329],[7.906388,47.558052],[7.91361,47.553886],[7.918888,47.552773],[7.931944,47.552216],[8.033333,47.556107],[8.086666,47.560555],[8.092222,47.561661],[8.099722,47.565552],[8.102499,47.568329],[8.103888,47.571938],[8.108055,47.578331],[8.110832,47.581108],[8.185276,47.617218],[8.193333,47.620827],[8.198889,47.621666],[8.205555,47.621666],[8.240833,47.619438],[8.283333,47.613052],[8.288055,47.611664],[8.2925,47.605827],[8.390833,47.579994],[8.435833,47.573608],[8.464998,47.602493],[8.477499,47.612221],[8.51111,47.631104],[8.519999,47.634163],[8.531387,47.632774],[8.558332,47.623329],[8.562222,47.621384],[8.565554,47.614441],[8.56943,47.598537],[8.57642,47.59137],[8.591389,47.599442],[8.594721,47.601662],[8.629166,47.646111],[8.630833,47.649719],[8.629444,47.653328],[8.626944,47.656105],[8.620832,47.66111],[8.610254,47.667732],[8.580555,47.66861],[8.551388,47.668327],[8.545832,47.667496],[8.536943,47.664444],[8.528055,47.656944],[8.525833,47.65361],[8.518332,47.649719],[8.495277,47.647217],[8.475555,47.646942],[8.424721,47.669441],[8.413332,47.678329],[8.407221,47.699715],[8.406666,47.703888],[8.473331,47.767494],[8.47611,47.769997],[8.479443,47.772499],[8.559721,47.806389],[8.566111,47.806938],[8.60861,47.804161],[8.614721,47.803604],[8.661388,47.79805],[8.685555,47.786659],[8.734999,47.761383],[8.736944,47.758049],[8.736944,47.75444],[8.734999,47.713882],[8.734165,47.709717],[8.730698,47.705673],[8.729166,47.703888],[8.720222,47.696625],[8.745277,47.695],[8.761389,47.692215],[8.800133,47.681725],[8.774721,47.710274],[8.772778,47.713333],[8.772221,47.717499],[8.777777,47.722771],[8.799999,47.735275],[8.804165,47.737221],[8.808887,47.735832],[8.835278,47.718048],[8.855833,47.698883],[8.883299,47.654148],[8.892776,47.651382],[8.904999,47.650276],[8.917776,47.650551],[8.934166,47.653053],[8.947777,47.657494],[8.973331,47.667496],[8.985554,47.672775],[8.996666,47.679161],[9.004999,47.682495],[9.024166,47.687775],[9.0425,47.688889],[9.078333,47.685829],[9.086943,47.684998],[9.261095,47.662842],[9.478322,47.575851],[9.566724,47.540451],[9.557772,47.505783],[9.559721,47.499718],[9.562222,47.496666],[9.567499,47.491104],[9.590277,47.470276],[9.594166,47.46833],[9.611666,47.468887],[9.646944,47.460274],[9.65111,47.458328],[9.65361,47.455551],[9.673332,47.398605],[9.673054,47.394997],[9.669998,47.387772],[9.664999,47.381386],[9.650555,47.373055],[9.641666,47.369995],[9.631388,47.36805],[9.625832,47.367218],[9.624422,47.367252],[9.614443,47.367493],[9.605276,47.359993],[9.599722,47.354164],[9.539999,47.284164],[9.533569,47.274544],[9.502222,47.231384],[9.491388,47.215271],[9.485832,47.200272],[9.484165,47.192215],[9.484444,47.179718],[9.489721,47.16555],[9.49861,47.15361],[9.505554,47.139999],[9.511389,47.121666],[9.512777,47.109444],[9.512499,47.100273],[9.510277,47.092499],[9.474637,47.057457],[9.485277,47.061104],[9.506388,47.06472],[9.538055,47.07],[9.544722,47.07],[9.598635,47.063835],[9.6325,47.056664],[9.719444,47.050278],[9.870277,47.023598],[9.875555,47.022491],[9.891943,47.003876],[9.893055,47.000267],[9.889444,46.993324],[9.880833,46.98082],[9.875555,46.970543],[9.873333,46.962769],[9.87361,46.958603],[9.87611,46.941658],[9.879444,46.939156],[9.934999,46.912773],[10.058887,46.866386],[10.109444,46.850273],[10.122776,46.849716],[10.140276,46.851105],[10.141807,46.851341],[10.15111,46.852776],[10.170555,46.857216],[10.22361,46.874992],[10.231388,46.878883],[10.313332,46.933609],[10.323332,46.955551],[10.345833,46.986382],[10.353611,46.994995],[10.357222,46.997215],[10.385555,47.002777],[10.391109,47.003609],[10.400833,47.001106],[10.429998,46.984161],[10.487499,46.937775],[10.488913,46.934212],[10.471235,46.871353],[10.447498,46.763054],[10.470833,46.635826],[10.486387,46.619438],[10.486666,46.615273],[10.486111,46.594719],[10.485277,46.590553],[10.475555,46.562492],[10.471109,46.551666],[10.465277,46.546387],[10.457531,46.542503],[10.44972,46.539162],[10.337221,46.549438],[10.305555,46.553886],[10.301666,46.555832],[10.247499,46.583885],[10.245554,46.587219],[10.245277,46.59111],[10.246944,46.599442],[10.253054,46.613884],[10.254166,46.61805],[10.253611,46.622215],[10.241665,46.636665],[10.238609,46.638885],[10.233332,46.639999],[10.18111,46.632217],[10.119165,46.611382],[10.0525,46.543053],[10.050278,46.539993],[10.044722,46.507217],[10.044167,46.498055],[10.044443,46.453331],[10.044998,46.449165],[10.046665,46.445831],[10.049166,46.443054],[10.055555,46.438332],[10.067221,46.432495],[10.08861,46.424164],[10.092777,46.423332],[10.153889,46.390274],[10.177221,46.272499],[10.176388,46.268326],[10.172499,46.257217],[10.169722,46.25444],[10.140833,46.23333],[10.133888,46.228882],[10.129999,46.227219],[10.078609,46.220551],[10.072777,46.220276],[10.066666,46.221107],[10.063055,46.222771],[10.05722,46.22805],[9.994999,46.291664],[9.982498,46.331383],[9.981943,46.334717],[9.991388,46.346664],[9.99361,46.354439],[9.991943,46.357773],[9.984722,46.361938],[9.949999,46.379166],[9.943888,46.379997],[9.91111,46.383331],[9.904999,46.383049],[9.852777,46.36972],[9.813055,46.35833],[9.770555,46.342209],[9.714998,46.302773],[9.708332,46.298332],[9.696943,46.297218],[9.624722,46.291939],[9.581944,46.296944],[9.547222,46.30555],[9.5425,46.306938],[9.521387,46.319717],[9.458611,46.38472],[9.456665,46.387772],[9.450277,46.426666],[9.455,46.472496],[9.372776,46.5075],[9.360832,46.508606],[9.302221,46.504715],[9.296944,46.503883],[9.288887,46.500275],[9.281944,46.495827],[9.273611,46.487778],[9.249722,46.448051],[9.25,46.440277],[9.253054,46.433327],[9.256943,46.431389],[9.272778,46.428329],[9.280554,46.423332],[9.282221,46.419998],[9.296944,46.35305],[9.297499,46.348885],[9.293333,46.32666],[9.291943,46.323051],[9.276667,46.292496],[9.24341,46.23671],[9.238609,46.230553],[9.189444,46.186661],[9.180555,46.179161],[9.177221,46.176941],[9.158609,46.171661],[9.144439,46.168049],[9.132221,46.160271],[9.083332,46.121109],[9.035831,46.05722],[9.016666,46.022499],[8.998055,45.976944],[8.996666,45.973328],[9.0175,45.94416],[9.025,45.935829],[9.031944,45.931664],[9.066666,45.924164],[9.079443,45.915833],[9.081388,45.912498],[9.085554,45.901939],[9.085554,45.899162],[9.083887,45.895554],[9.036665,45.837776],[8.998333,45.829437],[8.951111,45.845276],[8.93611,45.868889],[8.936666,45.872772],[8.936388,45.876938],[8.926666,45.900833],[8.9,45.949715],[8.895555,45.955826],[8.826666,45.984161],[8.813055,45.988609],[8.816944,46.031662],[8.82361,46.03611],[8.849998,46.054443],[8.852499,46.056938],[8.853333,46.061104],[8.850555,46.072495],[8.846666,46.079163],[8.844166,46.08194],[8.835278,46.089439],[8.826111,46.096664],[8.818333,46.100555],[8.747221,46.121109],[8.728855,46.108589],[8.72361,46.105553],[8.714998,46.102493],[8.701111,46.10083],[8.691944,46.101387],[8.617777,46.120277],[8.613888,46.122215],[8.460554,46.23333],[8.445555,46.245827],[8.444443,46.248604],[8.434166,46.291107],[8.434999,46.295273],[8.443333,46.317215],[8.447498,46.323326],[8.458332,46.33416],[8.465832,46.36805],[8.46611,46.37722],[8.461666,46.444443],[8.459999,46.452217],[8.451666,46.459999],[8.447498,46.461937],[8.436388,46.463333],[8.430277,46.463051],[8.365276,46.453606],[8.357222,46.449997],[8.30611,46.424721],[8.302776,46.422493],[8.299999,46.419716],[8.139999,46.226105],[8.143888,46.219719],[8.158333,46.188889],[8.159443,46.180832],[8.158888,46.176666],[8.150276,46.153053],[8.148888,46.149437],[8.014721,46.013329],[8.00861,46.008331],[7.998055,46.00222],[7.973055,45.996941],[7.95,45.994164],[7.911111,45.994164],[7.902499,45.991104],[7.881944,45.977776],[7.876666,45.972496],[7.866388,45.948326],[7.855742,45.919052],[7.8483,45.918755],[7.783889,45.923607],[7.743333,45.93222],[7.709167,45.943329],[7.653055,45.979996],[7.647499,45.98111],[7.536666,45.981667],[7.458055,45.941109],[7.429722,45.929443],[7.393332,45.916107],[7.378888,45.914719],[7.338888,45.920555],[7.300278,45.923882],[7.295555,45.92305],[7.291111,45.921944],[7.230277,45.898048],[7.196944,45.882217],[7.188611,45.879166],[7.183055,45.879166],[7.117499,45.880829],[7.105,45.882217],[7.099444,45.883606],[7.09111,45.887497],[7.077777,45.896942],[7.062778,45.909164],[7.038054,45.931938],[7.030555,45.961937],[7.008611,45.996666],[7.005833,45.999443],[6.933055,46.055275],[6.873888,46.088051],[6.788055,46.14222],[6.783889,46.148048],[6.781111,46.162498],[6.780833,46.166107],[6.799999,46.378326],[6.807016,46.404228],[6.809444,46.415276],[6.808332,46.41861],[6.803055,46.427773],[6.800278,46.43055],[6.791389,46.434166],[6.737778,46.447495],[6.702777,46.45472],[6.633611,46.464165],[6.52,46.45916],[6.510278,46.457771],[6.491944,46.453049],[6.328333,46.406944],[6.31111,46.401382],[6.295555,46.394165],[6.246388,46.357773],[6.243333,46.354996],[6.236388,46.344719],[6.23,46.333885],[6.225555,46.322495],[6.228055,46.319717],[6.243747,46.31551],[6.271944,46.26194],[6.245555,46.21833],[6.189166,46.173332],[6.185833,46.17083],[6.152222,46.15361],[6.133611,46.149437],[6.12361,46.148048],[5.996388,46.146942],[5.967222,46.201942],[5.96611,46.205276],[5.966666,46.209442],[5.968055,46.212494],[5.981388,46.221939],[6.009166,46.233887],[6.03,46.241386],[6.060833,46.250549],[6.072499,46.249718],[6.0825,46.246666],[6.107778,46.253052],[6.117222,46.260551],[6.119166,46.264442],[6.118055,46.268051],[6.106109,46.297489],[6.115,46.30555],[6.120277,46.311943],[6.154166,46.363052],[6.15861,46.369995],[6.158333,46.373886],[6.157222,46.37722],[6.151667,46.386383],[6.148333,46.388611],[6.111111,46.409721],[6.071111,46.426384],[6.086944,46.44944],[6.135278,46.539719],[6.129722,46.583054],[6.127222,46.585831],[6.127777,46.589996],[6.129999,46.59333],[6.269166,46.682777],[6.346666,46.711388],[6.355,46.714439],[6.366666,46.720551],[6.434999,46.758049],[6.452777,46.774437],[6.4575,46.78083],[6.458611,46.785271],[6.446944,46.839439],[6.460278,46.895828],[6.528333,46.971664],[6.566388,46.979996],[6.620832,46.994438],[6.629722,46.997215],[6.639722,47.004166],[6.678333,47.034164],[6.699444,47.063889],[6.793333,47.130829],[6.849721,47.164989],[6.838888,47.16777],[6.8375,47.171104],[6.839722,47.173607],[6.971666,47.291939],[7.000833,47.364998],[6.980833,47.361664],[6.931389,47.358887],[6.88111,47.356941],[6.879999,47.360275],[6.88111,47.364716],[6.884444,47.372772],[6.990555,47.497215],[7.025,47.508049],[7.14642,47.499054],[7.178333,47.445831],[7.187499,47.442215],[7.241111,47.421944],[7.246944,47.42083],[7.251389,47.421944],[7.275555,47.432495],[7.308332,47.440277],[7.336944,47.439438],[7.343611,47.438606],[7.355897,47.434017],[7.358055,47.433609],[7.3825,47.432495],[7.388333,47.434166],[7.415555,47.445274],[7.433054,47.460548],[7.452777,47.469994],[7.496944,47.494995],[7.502222,47.500549],[7.497222,47.544441],[7.49861,47.54805],[7.501389,47.550552],[7.513055,47.556664],[7.521667,47.559715],[7.546665,47.565552],[7.550833,47.576385],[7.557221,47.581383],[7.567222,47.583611],[7.586944,47.584999],[7.588268,47.58448],[7.640555,47.603882],[7.674444,47.606384],[7.676944,47.603607],[7.671944,47.579437],[7.669999,47.576385],[7.666666,47.574165],[7.621111,47.561104],[7.656111,47.550552],[7.697223,47.543327]],[[8.710255,47.696808],[8.709721,47.70694],[8.708332,47.710548],[8.705,47.713051],[8.698889,47.713608],[8.675278,47.712494],[8.670555,47.711105],[8.670277,47.707497],[8.673298,47.701771],[8.675554,47.697495],[8.678595,47.693344],[8.710255,47.696808]]],"type":"Polygon"},"properties":{"cca2":"ch"},"type":"Feature"}],"type":"FeatureCollection"}"""

  @Setup
  def setup(): Unit = {
    jsonBytes = jsonString1.getBytes(UTF_8)
    obj = readFromArray[GeoJSON.GeoJSON](jsonBytes)
    preallocatedBuf = new Array(jsonBytes.length + 100 /*to avoid possible out of bounds error*/)
  }
}