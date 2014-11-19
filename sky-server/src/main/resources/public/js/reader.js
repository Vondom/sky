sky.Reader = function () {
  this.fileReader = new FileReader();
};

sky.Reader.prototype = _.create(Object.prototype, {
  constructor: sky.Reader,
  readAsArrayBuffer: function (file) {

    if (_.isUndefined(file)) {
      throw new Error("file is not undefined");
    }

    var LIMIT = 10 * 1024 * 1024;

    if (file.size > LIMIT) {
      var pos = 0, results = [];

      while (pos < file.size) {
        var offset = min(file.size - pos, LIMIT);
        this.fileReader.readAsArrayBuffer(file.slice(pos, pos + offset));
        results.push(this.fileReader.result);
        pos += offset;
      }

      return _.flatten(results);
    } else {
      this.fileReader.readAsArrayBuffer(file);

      return this.fileReader.result;
    }
  },
  readAsDataURL: function (file) {
    this.fileReader.readAsDataURL(file);

    return this.fileReader.result;
  }
});

